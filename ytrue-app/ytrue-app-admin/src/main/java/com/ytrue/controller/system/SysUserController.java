package com.ytrue.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.query.system.SysUserQuery;
import com.ytrue.bean.req.system.SysUserReq;
import com.ytrue.bean.req.system.UpdatePasswordReq;
import com.ytrue.bean.req.system.UserProfileReq;
import com.ytrue.bean.resp.system.SysUserDetailResp;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.db.entity.Pageable;
import com.ytrue.service.system.SysUserService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.util.QueryHelp;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;
    private final static String DEFAULT_PASSWORD = "111111";


    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:user:page')")
    public ServerResponseEntity<IPage<SysUserListResp>> page(SysUserQuery params, Pageable pageable) {

        QueryEntity queryEntity = QueryHelp.queryEntityBuilder(params).addSort(SysUserListResp::getId, Boolean.FALSE);

        return ServerResponseEntity.success(sysUserService.paginate(pageable.page(), queryEntity));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:user:detail')")
    public ServerResponseEntity<SysUserDetailResp> detail(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysUserService.getUserById(id));
    }


    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:user:add')")
    public ServerResponseEntity<Void> add(@Validated @RequestBody SysUserReq sysUserReq) {
        sysUserReq.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        sysUserService.addUser(sysUserReq);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:user:update')")
    public ServerResponseEntity<Void> update(@Validated @RequestBody SysUserReq sysUserReq) {
        sysUserService.updateUser(sysUserReq);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:user:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids) {
        sysUserService.removeBatchUser(ids);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PostMapping("resetPassword")
    @Operation(summary = "重置密码")
    @PreAuthorize("@pms.hasPermission('system:user:restPassword')")
    public ServerResponseEntity<Void> resetPassword(@RequestParam Long userId) {
        sysUserService.lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getPassword, passwordEncoder.encode(DEFAULT_PASSWORD)).update();
        return ServerResponseEntity.success();
    }

    @PutMapping("updateUserProfile")
    @Operation(summary = "修改用户信息")
    public ServerResponseEntity<Void> updateProfile(@RequestBody UserProfileReq operation) {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();

        SysUser sysUser = sysUserService.getById(userId);
        sysUser.setEmail(operation.getEmail());
        sysUser.setPhone(operation.getPhone());
        sysUser.setGender(operation.getGender());
        sysUser.setNickName(operation.getNickName());
        sysUser.setAvatarPath(operation.getAvatarPath());

        sysUserService.updateById(sysUser);
        return ServerResponseEntity.success();
    }


    @PutMapping("updatePassword")
    @Operation(summary = "修改密码")
    public ServerResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordReq operation) {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();

        SysUser sysUser = sysUserService.getById(userId);
        // 旧密码错误
        AssertUtil.isTrue(passwordEncoder.matches(operation.getOldPassword(), sysUser.getPassword()), ServerResponseCode.error("旧密码错误"));
        // 新密码不能与旧密码相同
        AssertUtil.objectNotEquals(operation.getOldPassword(), operation.getNewPassword(), ServerResponseCode.error("新密码不能与旧密码相同"));
        // 设置密码
        sysUser.setPassword(passwordEncoder.encode(operation.getNewPassword()));
        sysUserService.updateById(sysUser);

        // 修改完成密码清除登录
        loginService.logout(userId);
        return ServerResponseEntity.success();
    }
}
