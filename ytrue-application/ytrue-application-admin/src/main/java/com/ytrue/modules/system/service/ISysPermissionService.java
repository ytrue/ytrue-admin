package com.ytrue.modules.system.service;

import com.ytrue.modules.system.model.po.SysUser;

import java.util.Set;

/**
 * @author ytrue
 * @description: ISysPermissionService
 * @date 2022/12/8 15:35
 */
public interface ISysPermissionService {

    /**
     * 获取用户角色code
     *
     * @param user
     * @return
     */
    Set<String> getRoleCode(SysUser user);


    /**
     * 获取用户权限
     *
     * @param user
     * @return
     */
    Set<String> getPermission(SysUser user);
}
