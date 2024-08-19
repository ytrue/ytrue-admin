package com.ytrue.dao.system;

import com.ytrue.infra.db.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysMenuDao
 * @date 2022/12/7 14:11
 */
public interface SysMenuDao extends IBaseDao<SysMenu> {


    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    @Select("""
            <script>
            SELECT
                    m.id
                    FROM
                    sys_menu m
                    LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id
                    WHERE
                    rm.role_id = #{roleId}
                    <if test="menuCheckStrictly">
                        AND m.id NOT IN (
                        SELECT
                        m.pid
                        FROM
                        sys_menu m
                        INNER JOIN sys_role_menu rm ON m.id = rm.menu_id
                        AND rm.role_id = #{roleId})
                    </if>
                    ORDER BY
                    m.pid,
                    m.menu_sort
            </script>
            """)
    Set<Long> selectMenuIdsBySysRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);


    /**
     * 根据用户ID查询菜单
     *
     * @param userId
     * @return
     */
    @Select("""
            <script>
             SELECT DISTINCT m.id,
                                    m.pid,
                                    m.menu_name,
                                    m.path,
                                    m.component,
                                    m.`query`,
                                    m.visible,
                                    m.STATUS,
                                    ifnull(m.perms, '') AS perms,
                                    m.is_frame,
                                    m.is_cache,
                                    m.menu_type,
                                    m.icon,
                                    m.menu_sort,
                                    m.create_time,
                                    m.sub_count
                    FROM sys_menu m
                             LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id
                             LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id
                             LEFT JOIN sys_role ro ON ur.role_id = ro.id
                             LEFT JOIN sys_user u ON ur.user_id = u.id
                    WHERE u.id = #{userId}
                      AND m.menu_type IN ('M' , 'C')
                      AND m.STATUS = 1
                    ORDER BY m.pid,
                        m.menu_sort
            </script>
            """)
    List<SysMenu> selectMenusBySysUserId(@Param("userId") Long userId);
}
