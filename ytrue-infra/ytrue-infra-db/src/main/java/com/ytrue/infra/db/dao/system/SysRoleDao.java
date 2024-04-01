package com.ytrue.infra.db.dao.system;

import com.ytrue.infra.db.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
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
    @Select("""
            <script>
              SELECT r.`role_code`
                    FROM sys_role r
                             LEFT JOIN sys_user_role sur ON sur.role_id = r.id
                             LEFT JOIN sys_user u ON u.id = sur.user_id
                    WHERE u.id = #{userId}
            </script>
            """)
    @ResultType(String.class)
    Set<String> listRoleCodeByUserId(Long userId);


    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    @Select("""
             SELECT r.*
                    FROM sys_role r
                             LEFT JOIN sys_user_role sur ON sur.role_id = r.id
                             LEFT JOIN sys_user u ON u.id = sur.user_id
                    WHERE u.id = 1
            """)
    Set<SysRole> listByUserId(@Param("userId") Long userId);
}
