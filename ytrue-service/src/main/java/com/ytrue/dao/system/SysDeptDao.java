package com.ytrue.dao.system;

import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.infra.db.base.IBaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysDeptDao
 * @date 2022/12/7 11:44
 */
public interface SysDeptDao extends IBaseDao<SysDept> {


    /**
     * 根据角色ID查询部门id
     *
     * @param roleId
     * @param deptCheckStrictly
     * @return
     */
    @Select("""
            <script>
             SELECT
                    d.id
                    FROM
                    sys_dept d
                    LEFT JOIN sys_role_dept rd ON d.id = rd.dept_id
                    WHERE
                    rd.role_id = #{roleId}
                    <if test="deptCheckStrictly">
                        AND d.id NOT IN (
                        SELECT
                        d.pid
                        FROM
                        sys_dept d
                        INNER JOIN sys_role_dept rd ON d.id = rd.dept_id
                        AND rd.role_id = #{roleId})
                    </if>
                    ORDER BY
                    d.pid,
                    d.dept_sort
            </script>
            """)
    @ResultType(Long.class)
    Set<Long> selectDeptIdsBySysRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);
}
