package com.ytrue.infra.security.permission;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.security.util.SecurityUtil;
import com.ytrue.infra.security.util.WhitelistMatcherUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @description: PermissionService
 * @date 2022/12/19 14:27
 */
@Slf4j
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
    private static final String ROLE_SEPARATOR = StrPool.COMMA;

    /**
     * 权限分隔符
     */
    private static final String PERMISSION_SEPARATOR = StrPool.COMMA;


    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permissions 权限
     * @return {boolean}
     */
    public boolean hasPermission(String permissions) {
        if (StrUtil.isBlank(permissions)) {
            return false;
        }

        // 检查路径是否在白名单中
        boolean isPathWhitelisted = WhitelistMatcherUtil.isPathWhitelisted(getHttpServletRequest());

        // 如果未登录，只有白名单路径才通过
        if (!SecurityUtil.isLogged()) {
            return isPathWhitelisted;
        }

        // 获取当前用户的权限集合
        Set<String> userAuthorities = SecurityUtil.getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());

        // 将权限字符串分隔并检查每个权限
        boolean hasRequiredPermissions = Arrays.stream(permissions.split(PERMISSION_SEPARATOR))
                .filter(StrUtil::isNotEmpty)
                .allMatch(permission -> checkPermission(userAuthorities, permission));

        // 如果用户拥有所需权限或路径在白名单中，返回 true
        return hasRequiredPermissions || isPathWhitelisted;
    }


    /**
     * 判断用户是否拥有某个角色
     *
     * @param roles 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String roles) {
        // 如果角色字符串为空或空白，直接返回 false
        if (StrUtil.isBlank(roles)) {
            return false;
        }

        // 检查路径是否在白名单中
        boolean isPathWhitelisted = WhitelistMatcherUtil.isPathWhitelisted(getHttpServletRequest());

        // 如果用户未登录，只有白名单路径才通过
        if (!SecurityUtil.isLogged()) {
            return isPathWhitelisted;
        }

        // 获取当前用户的角色集合
        Set<String> userRoles = SecurityUtil.getLoginUser().getUser().getRoles();
        if (CollectionUtils.isEmpty(userRoles)) {
            return false;
        }

        // 检查用户角色是否与提供的角色匹配
        boolean hasRequiredRole = Arrays.stream(roles.split(PERMISSION_SEPARATOR))
                .filter(StrUtil::isNotEmpty)
                .anyMatch(roleCode -> checkRole(userRoles, roleCode));

        // 如果用户具有所需角色或路径在白名单中，返回 true
        return hasRequiredRole || isPathWhitelisted;
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

    /**
     * 这个是 apo执行的可以拿到
     * @return
     */
    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }
}
