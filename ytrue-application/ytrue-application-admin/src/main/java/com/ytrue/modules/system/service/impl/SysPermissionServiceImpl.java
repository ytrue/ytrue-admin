package com.ytrue.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ytrue.modules.system.dao.SysRoleDao;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.service.ISysPermissionService;
import com.ytrue.tools.security.permission.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @description: SysPermissionServiceImpl
 * @date 2022/12/8 15:35
 */
@Service
@AllArgsConstructor
public class SysPermissionServiceImpl implements ISysPermissionService {

    private final SysRoleDao sysRoleDao;

    private final SysUserDao sysUserDao;

    @Override
    public Set<String> getRoleCode(SysUser user) {
        // 管理员拥有所有权限
        if (user.getAdmin()) {
            Set<String> roles = new HashSet<>();
            roles.add(PermissionService.SUPER_ADMIN);
            return roles;
        }
        return sysRoleDao.listRoleCodeByUserId(user.getId()).stream().filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getPermission(SysUser user) {
        // 管理员拥有所有权限
        if (user.getAdmin()) {
            Set<String> perms = new HashSet<>();
            perms.add(PermissionService.ALL_PERMISSION);
            return perms;
        }
        // 处理下逗号
        return sysUserDao.listPermsByUserId(user.getId())
                .stream().filter(StrUtil::isNotEmpty)
                .flatMap(s -> Arrays.stream(s.trim().split(",")))
                .collect(Collectors.toSet());
    }

}
