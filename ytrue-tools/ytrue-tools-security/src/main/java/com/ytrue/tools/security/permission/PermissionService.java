package com.ytrue.tools.security.permission;

import cn.hutool.core.util.StrUtil;
import com.ytrue.tools.security.user.LoginUser;
import com.ytrue.tools.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @description: PermissionService
 * @date 2022/12/19 14:27
 */
@Slf4j
@Component("pms")
public class PermissionService {

    /**
     * 所有权限标识
     */
    public static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    public static final String SUPER_ADMIN = "*";

    /**
     * 角色分隔符
     */
    private static final String ROLE_SEPARATOR = ",";

    /**
     * 权限分隔符
     */
    private static final String PERMISSION_SEPARATOR = ",";


    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permissions 权限
     * @return {boolean}
     */
    public boolean hasPermission(String permissions) {

        if (!baseCheck(permissions)) {
            return false;
        }

        // 获取当前用的权限
        Set<String> authorities = SecurityUtils.getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText).collect(Collectors.toSet());

        // 循环判断
        for (String permission : permissions.split(PERMISSION_SEPARATOR)) {
            if (permission != null && checkPermission(authorities, permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断用户是否拥有某个角色
     *
     * @param roles 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String roles) {
        if (!baseCheck(roles)) {
            return false;
        }

        Set<String> roleCodes = SecurityUtils.getLoginUser().getUser().getRoles();
        if (CollectionUtils.isEmpty(roleCodes)) {
            return false;
        }
        // 循环判断
        for (String role : roles.split(ROLE_SEPARATOR)) {
            if (role != null && checkRole(roleCodes, role)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 基础校验
     *
     * @param value
     * @return
     */
    private boolean baseCheck(String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }

        return SecurityUtils.getAuthentication() != null;
    }


    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色字符串
     * @return 用户是否具备某角色
     */
    private boolean checkRole(Set<String> roles, String role) {
        return roles.contains(SUPER_ADMIN) || roles.contains(StrUtil.trim(role));
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean checkPermission(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StrUtil.trim(permission));
    }
}
