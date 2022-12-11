package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.SysRoleMenu;
import org.apache.ibatis.annotations.Insert;
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
     * <p>
     * insert into sys_role_menu (role_id,menu_id) values
     * <foreach collection="menuIds" item="menuId" separator=",">
     * (#{roleId},#{menuId})
     * </foreach>
     *
     * @param roleId
     * @param menuIds
     */
    @Insert("insert into sys_role_menu (role_id,menu_id) values <foreach collection='menuIds' item='menuId' separator=','>(#{roleId},#{menuId})</foreach>")
    void insertBatchRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Set<Long> menuIds);
}
