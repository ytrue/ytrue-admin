package com.ytrue.controller.system;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.resp.system.LoginUserInfoResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.service.system.*;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.util.SecurityUtils;
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
    public ServerResponseEntity<LoginUserInfoResp> getUserInfo() {
        String username = SecurityUtils.getLoginUser().getUsername();
        // 获取用户
        SysUser sysUser = sysUserService.getUserByUsername(username);
        // 获取岗位
        List<SysJob> sysJobs = sysJobService.listByUserId(sysUser.getId());
        // 获取部门
        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        // 获取角色
        Set<SysRole> sysRoles = sysRoleService.listByUserId(sysUser.getId());
        // 权限集合
        Set<String> permissions = sysPermissionService.getPermission(sysUser);
        // 角色集合
        Set<String> roleCodes = sysPermissionService.getRoleCode(sysUser);

        LoginUserInfoResp sysUserInfoVO = new LoginUserInfoResp();
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
    public ServerResponseEntity<List<Tree<String>>> getRouters() {
        List<Tree<String>> menuTree = sysMenuService.listMenuTreeByUserId(
                Convert.toLong(SecurityUtils.getLoginUser().getUser().getUserId()));
        return ServerResponseEntity.success(menuTree);
    }

}
