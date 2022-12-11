package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.SysUserJob;
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
     * <p>
     * insert into sys_user_job (user_id,job_id) values
     * <foreach collection="jobIds" item="jobId" separator=",">
     * (#{userId},#{jobId})
     * </foreach>
     *
     * @param userId
     * @param jobIds
     */
    @Insert("insert into sys_user_job (user_id,job_id) values <foreach collection='jobIds' item='jobId' separator=','>(#{userId},#{jobId})</foreach>")
    void insertBatchUserJob(@Param("userId") Long userId, @Param("jobIds") Set<Long> jobIds);
}
