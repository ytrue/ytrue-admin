package com.ytrue.modules.system.dao;

import com.ytrue.db.mybatis.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysJob;

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
    List<SysJob> listByUserId(Long userId);
}
