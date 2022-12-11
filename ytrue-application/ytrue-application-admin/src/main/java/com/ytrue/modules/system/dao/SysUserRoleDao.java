package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.SysUserRole;
import org.apache.ibatis.annotations.Insert;
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
     * <p>
     * insert into sys_user_role (user_id,role_id) values
     * <foreach collection="roleIds" item="roleId" separator=",">
     * (#{userId},#{roleId})
     * </foreach>
     *
     * @param userId
     * @param roleIds
     */
    @Insert("insert into sys_user_role (user_id,role_id) values <foreach collection='roleIds' item='roleId' separator=','>(#{userId},#{roleId})</foreach>")
    void insertBatchUserRole(@Param("userId") Long userId, @Param("roleIds") Set<Long> roleIds);
}
