package com.ytrue.modules.system.dao;

import com.ytrue.db.mybatis.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserRoleDao
 * @date 2022/12/7 17:20
 */
public interface SysUserRoleDao extends IBaseDao<SysUserRole> {

    /**
     * 根据用户id 批量添加用户与角色关系
     * @param userId
     * @param roleIds
     */
   void insertBatchUserRole(@Param("userId") Long userId, @Param("roleIds") Set<Long> roleIds);
}
