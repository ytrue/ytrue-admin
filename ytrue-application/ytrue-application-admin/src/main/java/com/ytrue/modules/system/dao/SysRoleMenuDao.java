package com.ytrue.modules.system.dao;

import com.ytrue.db.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleMenuDao
 * @date 2022/12/7 15:57
 */
public interface SysRoleMenuDao extends IBaseDao<SysRoleMenu> {


    /**
     * 根据角色id 批量添加角色与菜单关系
     *
     * @param roleId
     * @param menuIds
     */
    void insertBatchRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Set<Long> menuIds);
}
