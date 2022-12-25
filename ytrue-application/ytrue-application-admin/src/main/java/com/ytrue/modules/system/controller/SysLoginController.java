package com.ytrue.modules.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.vo.SysUserInfoVO;
import com.ytrue.modules.system.service.ISysMenuService;
import com.ytrue.modules.system.service.ISysPermissionService;
import com.ytrue.modules.system.service.ISysUserService;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "后台登录")
@RestController
@AllArgsConstructor
public class SysLoginController {

    private final LoginService loginService;

    private final ISysPermissionService sysPermissionService;

    private final ISysUserService sysUserService;

    private final ISysMenuService sysMenuService;


    @ApiOperation("登录")
    @PostMapping("/login")
    public ApiResultResponse<Map<String, String>> login() {
        return ApiResultResponse.success(loginService.login());
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public ApiResultResponse<Object> logout() {
        loginService.logout(SecurityUtils.getLoginUser().getUser().getUserId());
        return ApiResultResponse.success();
    }

    @ApiOperation("用户信息")
    @GetMapping("/getInfo")
    public ApiResultResponse<SysUserInfoVO> getUserInfo() {
        String username = SecurityUtils.getLoginUser().getUsername();
        // 获取用户
        SysUser sysUser = sysUserService.getUserByUsername(username);
        // 角色集合
        Set<String> roles = sysPermissionService.getRoleCode(sysUser);
        // 权限集合
        Set<String> permissions = sysPermissionService.getPermission(sysUser);

        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        sysUserInfoVO.setUser(sysUser);
        sysUserInfoVO.setRoles(roles);
        sysUserInfoVO.setPermissions(permissions);

        return ApiResultResponse.success(sysUserInfoVO);
    }

    @ApiOperation("路由信息")
    @GetMapping("getRouters")
    public ApiResultResponse<List<Tree<String>>> getRouters() {
        List<Tree<String>> menuTree = sysMenuService.listMenuTreeByUserId(Convert.toLong(SecurityUtils.getLoginUser().getUser().getUserId()));
        return ApiResultResponse.success(menuTree);
    }

}
