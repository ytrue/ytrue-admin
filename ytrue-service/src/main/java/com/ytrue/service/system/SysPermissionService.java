package com.ytrue.service.system;

import com.ytrue.bean.dataobject.system.SysUser;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysPermissionService
 * @date 2022/12/8 15:35
 */
public interface SysPermissionService {

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
