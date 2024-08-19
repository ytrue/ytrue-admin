package com.ytrue.dao.system;

import com.ytrue.infra.db.base.IBaseDao;
import com.ytrue.bean.dataobject.system.SysJob;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ytrue
 * @description: SysJobDao
 * @date 2022/12/7 10:55
 */
public interface SysJobDao extends IBaseDao<SysJob> {

    /**
     * 根据用户id获取对应的岗位
     *
     * @param userId
     * @return
     */
    @Select("""
            SELECT j.*
                  FROM sys_job j
                  INNER JOIN sys_user_job AS uj ON j.id = uj.job_id WHERE uj.user_id = #{userId}
              """)
    List<SysJob> selectBySysUserId(Long userId);
}
