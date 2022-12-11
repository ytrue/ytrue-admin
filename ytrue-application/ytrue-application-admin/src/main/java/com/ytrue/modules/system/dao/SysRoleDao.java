package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.SysRole;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleDao
 * @date 2022/12/7 15:22
 */
public interface SysRoleDao extends IBaseDao<SysRole> {


    /**
     * 根据用户ID查询角色code
     *
     * @param userId 用户ID
     * @return 角色code列表
     */
    @Select("SELECT r.`code` FROM sys_role r LEFT JOIN sys_user_role ur ON ur.role_id = r.id LEFT JOIN sys_user u ON u.id = ur.user_id WHERE u.id = ${userId}}")
    Set<String> selectRoleCodeByUserId(Long userId);
}
