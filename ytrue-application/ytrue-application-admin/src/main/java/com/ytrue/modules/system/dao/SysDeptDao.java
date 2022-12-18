package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysDeptDao
 * @date 2022/12/7 11:44
 */
public interface SysDeptDao extends IBaseDao<SysDept> {


    /**
     * 根据角色ID查询部门id
     *
     * @param roleId
     * @param deptCheckStrictly
     * @return
     */
    Set<Long> listDeptIdByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);
}
