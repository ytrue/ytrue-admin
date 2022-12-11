package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuDao
 * @date 2022/12/7 14:11
 */
public interface SysMenuDao extends IBaseDao<SysMenu> {


    /**
     * 根据用户ID查询菜单
     *
     * @param userId
     * @return
     */
    @Select("SELECT DISTINCT\n" +
            "\tm.id,\n" +
            "\tm.pid,\n" +
            "\tm.menu_name,\n" +
            "\tm.path,\n" +
            "\tm.component,\n" +
            "\tm.`query`,\n" +
            "\tm.visible,\n" +
            "\tm.STATUS,\n" +
            "\tifnull( m.perms, '' ) AS perms,\n" +
            "\tm.is_frame,\n" +
            "\tm.is_cache,\n" +
            "\tm.menu_type,\n" +
            "\tm.icon,\n" +
            "\tm.menu_sort,\n" +
            "\tm.create_time \n" +
            "FROM\n" +
            "\tsys_menu m\n" +
            "\tLEFT JOIN sys_role_menu rm ON m.id = rm.menu_id\n" +
            "\tLEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id\n" +
            "\tLEFT JOIN sys_role ro ON ur.role_id = ro.id\n" +
            "\tLEFT JOIN sys_user u ON ur.user_id = u.id \n" +
            "WHERE\n" +
            "\tu.id = #{userId} \n" +
            "\tAND m.menu_type IN ( 'M', 'C' ) \n" +
            "\tAND m.STATUS = 1 \n" +
            "ORDER BY\n" +
            "\tm.pid,\n" +
            "\tm.menu_sort")
    List<SysMenu> selectMenuTreeByUserId(Long userId);
}
