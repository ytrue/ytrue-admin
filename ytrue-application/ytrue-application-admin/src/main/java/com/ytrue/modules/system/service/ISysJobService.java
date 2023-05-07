package com.ytrue.modules.system.service;

import com.ytrue.db.mybatis.base.IBaseService;
import com.ytrue.modules.system.model.po.SysJob;

import java.util.List;

/**
 * @author ytrue
 * @description: ISysJobService
 * @date 2022/12/7 10:56
 */
public interface ISysJobService extends IBaseService<SysJob> {

    /**
     * 根据用户id获取对应的岗位
     *
     * @param userId
     * @return
     */
    List<SysJob> listByUserId(Long userId);
}
