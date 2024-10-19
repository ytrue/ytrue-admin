package com.ytrue.repository.mysql.system;

import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.infra.mysql.base.IBaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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
    Set<String> selectRoleCodeBySysUserId(Long userId);


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
    List<SysRole> selectBySysUserId(@Param("userId") Long userId);
}
