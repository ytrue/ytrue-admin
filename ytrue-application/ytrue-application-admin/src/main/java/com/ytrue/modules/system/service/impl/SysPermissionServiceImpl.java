package com.ytrue.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ytrue.modules.system.dao.SysRoleDao;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.model.SysUser;
import com.ytrue.modules.system.service.ISysPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
        if (user.getIsAdmin()) {
            Set<String> roles = new HashSet<>();
            roles.add("admin");
            return roles;
        }
        return sysRoleDao.selectRoleCodeByUserId(user.getId()).stream().filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getPermission(SysUser user) {
        // 管理员拥有所有权限
        if (user.getIsAdmin()) {
            Set<String> perms = new HashSet<>();
            perms.add("*:*:*");
            return perms;
        }
        // 返回对应的
        return sysUserDao.selectPermsByUserId(user.getId()).stream().filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
    }


}
