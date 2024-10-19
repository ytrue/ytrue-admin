package com.ytrue.repository.mysql.system;

import com.ytrue.infra.mysql.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysUserJob;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserJobDao
 * @date 2022/12/7 17:20
 */
public interface SysUserJobDao extends IBaseDao<SysUserJob> {

    /**
     * 根据用户id 批量添加用户与岗位关系
     *
     * @param userId
     * @param jobIds
     */
    @Insert("""
            <script>
             insert into sys_user_job (user_id,job_id) values
                    <foreach collection="jobIds" item="jobId" separator=",">
                           (#{userId},#{jobId})
                    </foreach>
            </script>
            """)
    void insertBatchUserJob(@Param("userId") Long userId, @Param("jobIds") Set<Long> jobIds);
}
