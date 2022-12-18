package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysUser;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserDao
 * @date 2022/12/7 16:57
 */
public interface SysUserDao extends IBaseDao<SysUser> {


    /**
     * 查询用户的所有权限
     *
     * @param userId
     * @return
     */
    Set<String> listPermsByUserId(Long userId);
}
