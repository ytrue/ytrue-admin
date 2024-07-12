package com.ytrue.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.query.system.SysUserPageQuery;
import com.ytrue.bean.req.system.SysUserAddReq;
import com.ytrue.bean.req.system.SysUserUpdatePasswordReq;
import com.ytrue.bean.req.system.SysUserUpdateProfileReq;
import com.ytrue.bean.req.system.SysUserUpdateReq;
import com.ytrue.bean.resp.system.SysUserIdResp;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.log.annotation.OperateLog;
import com.ytrue.service.system.SysUserService;
import com.ytrue.infra.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:user:page')")
    public ServerResponseEntity<IPage<SysUserListResp>> listBySysUserPageQuery(SysUserPageQuery queryParam) {
        return ServerResponseEntity.success(sysUserService.listBySysUserPageQuery(queryParam));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:user:detail')")
    public ServerResponseEntity<SysUserIdResp> getBySysUserId(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysUserService.getBySysUserId(id));
    }


    @OperateLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:user:add')")
    public ServerResponseEntity<Void> addSysUser(@Validated @RequestBody SysUserAddReq requestParam) {
        sysUserService.addSysUser(requestParam);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:user:update')")
    public ServerResponseEntity<Void> updateSysUser(@Validated @RequestBody SysUserUpdateReq requestParam) {
        sysUserService.updateSysUser(requestParam);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:user:delete')")
    public ServerResponseEntity<Void> removeBatchBySysUserIds(@RequestBody List<Long> ids) {
        sysUserService.removeBySysUserIds(ids);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @PostMapping("resetPassword")
    @Operation(summary = "重置密码")
    @PreAuthorize("@pms.hasPermission('system:user:restPassword')")
    public ServerResponseEntity<Void> resetPassword(@RequestParam Long userId) {
        sysUserService.resetSysUserPassword(userId);
        return ServerResponseEntity.success();
    }

    @PutMapping("updateUserProfile")
    @Operation(summary = "修改用户信息")
    public ServerResponseEntity<Void> updateProfile(@RequestBody SysUserUpdateProfileReq requestParam) {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();

        SysUser sysUser = sysUserService.getById(userId);
        sysUser.setEmail(requestParam.getEmail());
        sysUser.setPhone(requestParam.getPhone());
        sysUser.setGender(requestParam.getGender());
        sysUser.setNickName(requestParam.getNickName());
        sysUser.setAvatarPath(requestParam.getAvatarPath());

        sysUserService.updateById(sysUser);
        return ServerResponseEntity.success();
    }


    @PutMapping("updatePassword")
    @Operation(summary = "修改密码")
    public ServerResponseEntity<Void> updateSysUserPassword(@RequestBody SysUserUpdatePasswordReq requestParam) {
        sysUserService.updateSysUserPassword(requestParam);
        return ServerResponseEntity.success();
    }
}
