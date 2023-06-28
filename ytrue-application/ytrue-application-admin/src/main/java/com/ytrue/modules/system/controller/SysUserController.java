package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.db.entity.Pageable;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.query.SysUserQuery;
import com.ytrue.modules.system.model.req.SysUserReq;
import com.ytrue.modules.system.model.req.UpdatePasswordReq;
import com.ytrue.modules.system.model.req.UserProfileReq;
import com.ytrue.modules.system.model.res.SysUserDetailRes;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.res.SysUserListRes;
import com.ytrue.modules.system.service.ISysUserService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.utils.QueryHelp;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ytrue
 * @description: SysUserController
 * @date 2022/12/7 16:59
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("sys/user")
@AllArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    private final LoginService loginService;

    private final static String DEFAULT_PASSWORD = "111111";


    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:user:page')")
    public ApiResultResponse<IPage<SysUserListRes>> page(SysUserQuery params, Pageable pageable) {

        QueryEntity queryEntity = QueryHelp.queryEntityBuilder(params).addSort(SysUserListRes::getId, false);

        return ApiResultResponse.success(sysUserService.paginate(pageable.page(), queryEntity));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:user:detail')")
    public ApiResultResponse<SysUserDetailRes> detail(@PathVariable("id") Long id) {
        return ApiResultResponse.success(sysUserService.getUserById(id));
    }


    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:user:add')")
    public ApiResultResponse<Object> add(@Validated @RequestBody SysUserReq sysUserReq) {
        sysUserReq.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        sysUserService.addUser(sysUserReq);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:user:update')")
    public ApiResultResponse<Object> update(@Validated @RequestBody SysUserReq sysUserReq) {
        sysUserService.updateUser(sysUserReq);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:user:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysUserService.removeBatchUser(ids);
        return ApiResultResponse.success();
    }

    @SysLog
    @PostMapping("resetPassword")
    @Operation(summary = "重置密码")
    @PreAuthorize("@pms.hasPermission('system:user:restPassword')")
    public ApiResultResponse<Object> resetPassword(@RequestParam Long userId) {
        sysUserService.lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getPassword, passwordEncoder.encode(DEFAULT_PASSWORD)).update();
        return ApiResultResponse.success();
    }

    @PutMapping("updateUserProfile")
    @Operation(summary = "修改用户信息")
    public ApiResultResponse<Object> updateProfile(@RequestBody UserProfileReq operation) {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();

        SysUser sysUser = sysUserService.getById(userId);
        sysUser.setEmail(operation.getEmail());
        sysUser.setPhone(operation.getPhone());
        sysUser.setGender(operation.getGender());
        sysUser.setNickName(operation.getNickName());
        sysUser.setAvatarPath(operation.getAvatarPath());

        sysUserService.updateById(sysUser);
        return ApiResultResponse.success();
    }


    @PutMapping("updatePassword")
    @Operation(summary = "修改密码")
    public ApiResultResponse<Object> updatePassword(@RequestBody UpdatePasswordReq operation) {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();

        SysUser sysUser = sysUserService.getById(userId);
        // 旧密码错误
        AssertUtils.isTrue(passwordEncoder.matches(operation.getOldPassword(), sysUser.getPassword()), ResponseCode.OLD_PASSWORD_ERROR);
        // 新密码不能与旧密码相同
        AssertUtils.noteEquals(operation.getOldPassword(), operation.getNewPassword(), ResponseCode.NEW_PASS_NOT_EQUAL_OLD_PASS);
        // 设置密码
        sysUser.setPassword(passwordEncoder.encode(operation.getNewPassword()));
        sysUserService.updateById(sysUser);

        // 修改完成密码清除登录
        loginService.logout(userId);
        return ApiResultResponse.success();
    }
}
