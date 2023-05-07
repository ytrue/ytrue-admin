package com.ytrue.modules.system.dao;

import com.ytrue.db.mybatis.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysRoleDept;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleDeptDao
 * @date 2022/12/7 15:57
 */
public interface SysRoleDeptDao extends IBaseDao<SysRoleDept> {

    /**
     * 根据角色id 批量添加角色与部门关系
     * @param roleId
     * @param deptIds
     */
   void insertBatchRoleDept(@Param("roleId") Long roleId, @Param("deptIds") Set<Long> deptIds);
}
