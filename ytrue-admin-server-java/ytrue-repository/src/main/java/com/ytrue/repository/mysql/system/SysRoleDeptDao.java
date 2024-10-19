package com.ytrue.repository.mysql.system;

import com.ytrue.infra.mysql.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysRoleDept;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleDeptDao
 * @date 2022/12/7 15:57
 */
public interface SysRoleDeptDao extends IBaseDao<SysRoleDept> {

    /**
     * 根据角色id 批量添加角色与部门关系
     *
     * @param roleId
     * @param deptIds
     */
    @Insert("""
            <script>
              insert into sys_role_dept (role_id,dept_id) values
                    <foreach collection='deptIds' item='deptId' separator=','>
                         (#{roleId},#{deptId})
                    </foreach>
            </script>
            """)
    void insertBatchRoleDept(@Param("roleId") Long roleId, @Param("deptIds") Set<Long> deptIds);
}
