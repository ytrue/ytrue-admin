package com.ytrue.service.system;

import com.ytrue.bean.req.system.SysJobAddReq;
import com.ytrue.bean.req.system.SysJobUpdateReq;
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
    List<SysJob> listBySysUserId(Long userId);

    /**
     * 新增岗位
     *
     * @param requestParam
     */
    void addSysJob(SysJobAddReq requestParam);

    /**
     * 更新岗位
     *
     * @param requestParam
     */
    void updateSysJob(SysJobUpdateReq requestParam);

    /**
     * 删除岗位
     * @param ids
     */
    void removeBatchSysJobByIds(List<Long> ids);

    /**
     * 根据id获取岗位
     * @param id
     * @return
     */
    SysJob getSysJobById(Long id);
}
