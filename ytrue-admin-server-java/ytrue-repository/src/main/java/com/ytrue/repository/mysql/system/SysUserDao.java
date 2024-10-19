package com.ytrue.repository.mysql.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.infra.mysql.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysUser;
import org.apache.ibatis.annotations.Param;
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
     * @param userId
     * @return
     */
    @Select("""
            <script>
             SELECT m.perms
                    FROM sys_user_role ur
                             INNER JOIN sys_role_menu rm ON ur.role_id = rm.role_id
                             INNER JOIN sys_menu m ON rm.menu_id = m.id
                    WHERE ur.user_id = #{userId}
            </script>
            """)
    Set<String> selectPermsBySysUserId(Long userId);


    /**
     * 列表查询
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("""
            <script>
            SELECT u.id,
                           d.dept_name,
                           u.username,
                           u.nick_name,
                           u.email,
                           u.phone,
                           u.gender,
                           u.avatar,
                           u.`status`,
                           u.admin,
                           u.create_time
                    FROM sys_user u
                    LEFT JOIN sys_dept d ON u.dept_id = d.id 
                    ${ew.customSqlSegment}
            </script>
            """)
    IPage<SysUserListResp> selectWithDeptName(IPage<SysUserListResp> page, @Param("ew") QueryWrapper<?> queryWrapper);
}
