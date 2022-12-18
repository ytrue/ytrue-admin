package com.ytrue.modules.system.dao;

import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysUserJob;
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
     * @param userId
     * @param jobIds
     */
   void insertBatchUserJob(@Param("userId") Long userId, @Param("jobIds") Set<Long> jobIds);
}
