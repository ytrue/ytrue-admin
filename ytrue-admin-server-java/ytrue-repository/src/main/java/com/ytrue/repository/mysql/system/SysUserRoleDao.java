package com.ytrue.repository.mysql.system;

import com.ytrue.infra.mysql.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysUserRole;
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
     *
     * @param userId
     * @param roleIds
     */
    @Insert("""
            <script>
             insert into sys_user_role (user_id,role_id) values
                    <foreach collection="roleIds" item="roleId" separator=",">
                          (#{userId},#{roleId})
                    </foreach>
            </script>
            """)
    void insertBatchUserRole(@Param("userId") Long userId, @Param("roleIds") Set<Long> roleIds);
}
