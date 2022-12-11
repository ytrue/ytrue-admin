package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.SysUser;
import org.apache.ibatis.annotations.Select;

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
     * @param userId 用户ID
     * @return
     */
    @Select("SELECT m.perms FROM sys_user_role ur INNER JOIN sys_role_menu rm ON ur.role_id = rm.role_id INNER JOIN sys_menu m ON rm.menu_id = m.id WHERE ur.user_id = #{userId} ")
    Set<String> selectPermsByUserId(Long userId);
}
