package com.ytrue.modules.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.model.po.SysJob;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.vo.SysUserInfoVO;
import com.ytrue.modules.system.service.*;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SysLoginController {

    private final LoginService loginService;

    private final ISysPermissionService sysPermissionService;

    private final ISysUserService sysUserService;

    private final ISysMenuService sysMenuService;

    private final ISysDeptService sysDeptService;

    private final ISysRoleService sysRoleService;

    private final ISysJobService sysJobService;


    @Operation(summary="登录")
    @PostMapping("/login")
    public ApiResultResponse<Map<String, String>> login() {
        return ApiResultResponse.success(loginService.login());
    }

    @Operation(summary="登出")
    @PostMapping("/logout")
    public ApiResultResponse<Object> logout() {
        loginService.logout(SecurityUtils.getLoginUser().getUser().getUserId());
        return ApiResultResponse.success();
    }

    @Operation(summary="用户信息")
    @GetMapping("/getInfo")
    public ApiResultResponse<SysUserInfoVO> getUserInfo() {
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

        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        sysUserInfoVO.setUser(sysUser);
        sysUserInfoVO.setJobs(sysJobs);
        sysUserInfoVO.setDept(sysDept);
        sysUserInfoVO.setRoles(sysRoles);
        sysUserInfoVO.setPermissions(permissions);
        sysUserInfoVO.setRoleCodes(roleCodes);

        return ApiResultResponse.success(sysUserInfoVO);
    }

    @Operation(summary="路由信息")
    @GetMapping("getRouters")
    public ApiResultResponse<List<Tree<String>>> getRouters() {
        List<Tree<String>> menuTree = sysMenuService.listMenuTreeByUserId(Convert.toLong(SecurityUtils.getLoginUser().getUser().getUserId()));
        return ApiResultResponse.success(menuTree);
    }

}
