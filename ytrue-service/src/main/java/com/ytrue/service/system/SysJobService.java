package com.ytrue.service.system;

import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.bean.dataobject.system.SysJob;

import java.util.List;

/**
 * @author ytrue
 * @description: SysJobService
 * @date 2022/12/7 10:56
 */
public interface SysJobService extends IBaseService<SysJob> {

    /**
     * 根据用户id获取对应的岗位
     *
     * @param userId
     * @return
     */
    List<SysJob> listByUserId(Long userId);
}
