package com.ytrue.dao.system;

import com.ytrue.bean.dataobject.system.SysRoleMenu;
import com.ytrue.infra.db.base.IBaseDao;
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
     *
     * @param roleId
     * @param menuIds
     */
    @Insert("""
            <script>
              insert into sys_role_menu (role_id,menu_id) values
                    <foreach collection="menuIds" item="menuId" separator=",">
                         (#{roleId},#{menuId})
                    </foreach>
            </script>
            """)
    void insertBatchRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Set<Long> menuIds);
}
