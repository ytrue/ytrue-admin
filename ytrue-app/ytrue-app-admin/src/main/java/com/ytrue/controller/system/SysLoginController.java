package com.ytrue.controller.system;

import cn.hutool.core.lang.tree.Tree;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.resp.system.SysUserLoginInfoResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.service.system.*;
import com.ytrue.infra.security.service.LoginService;
import com.ytrue.infra.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysLoginController
 * @date 2022/12/8 9:08
 */
@Tag(name = "后台登录")
@RestController
@RequiredArgsConstructor
public class SysLoginController {

    private final LoginService loginService;
    private final SysPermissionService sysPermissionService;
    private final SysUserService sysUserService;
    private final SysMenuService sysMenuService;
    private final SysDeptService sysDeptService;
    private final SysRoleService sysRoleService;
    private final SysJobService sysJobService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ServerResponseEntity<Map<String, String>> login() {
        return ServerResponseEntity.success(loginService.login());
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public ServerResponseEntity<Void> logout() {
        loginService.logout(SecurityUtils.getLoginUser().getUser().getUserId());
        return ServerResponseEntity.success();
    }

    @Operation(summary = "用户信息")
    @GetMapping("/getInfo")
    public ServerResponseEntity<SysUserLoginInfoResp> getSysUserLoginInfo() {
        String currLoginUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        // 根据用户名获取用户
        SysUser sysUser = sysUserService.getById(Long.valueOf(currLoginUserId));
        // 获取岗位
        List<SysJob> sysJobs = sysJobService.listBySysUserId(sysUser.getId());
        // 获取部门
        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        // 获取角色
        Set<SysRole> sysRoles = sysRoleService.listBySysUserId(sysUser.getId());
        // 权限集合
        Set<String> permissions = sysPermissionService.listPermissionBySysUser(sysUser);
        // 角色集合
        Set<String> roleCodes = sysPermissionService.listRoleCodeBySysUser(sysUser);

        SysUserLoginInfoResp sysUserInfoVO = new SysUserLoginInfoResp();
        sysUserInfoVO.setUser(sysUser);
        sysUserInfoVO.setJobs(sysJobs);
        sysUserInfoVO.setDept(sysDept);
        sysUserInfoVO.setRoles(sysRoles);
        sysUserInfoVO.setPermissions(permissions);
        sysUserInfoVO.setRoleCodes(roleCodes);

        return ServerResponseEntity.success(sysUserInfoVO);
    }

    @Operation(summary = "路由信息")
    @GetMapping("getRouters")
    public ServerResponseEntity<List<Tree<String>>> listMenuTreeBySysUserId() {
        String currLoginUserId = SecurityUtils.getLoginUser().getUser().getUserId();

        List<Tree<String>> menuTree = sysMenuService.listMenuTreeBySysUserId(Long.valueOf(currLoginUserId));
        return ServerResponseEntity.success(menuTree);
    }

}
