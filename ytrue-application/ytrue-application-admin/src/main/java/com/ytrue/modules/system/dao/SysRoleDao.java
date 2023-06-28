package com.ytrue.modules.system.dao;

import com.ytrue.db.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysRole;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleDao
 * @date 2022/12/7 15:22
 */
public interface SysRoleDao extends IBaseDao<SysRole> {


    /**
     * 根据用户ID查询角色code
     *
     * @param userId 用户ID
     * @return 角色code列表
     */
    Set<String> listRoleCodeByUserId(Long userId);


    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    Set<SysRole> listByUserId(Long userId);
}
