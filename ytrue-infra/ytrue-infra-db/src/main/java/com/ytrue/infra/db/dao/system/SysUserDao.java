package com.ytrue.infra.db.dao.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.infra.db.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.tools.query.entity.QueryEntity;
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
    Set<String> listPermsByUserId(Long userId);


    /**
     * 列表查询
     *
     * @param page
     * @param queryEntity
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
                           u.avatar_path,
                           u.`status`,
                           u.create_time,
                           u.admin
                    FROM sys_user u
                             LEFT JOIN sys_dept d ON u.dept_id = d.id
            </script>
            """)
    IPage<SysUserListResp> listWithDeptName(IPage<SysUserListResp> page, QueryEntity queryEntity);
}
