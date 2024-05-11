package com.ytrue.service.system;

import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.query.system.SysDeptListQuery;
import com.ytrue.bean.req.system.SysDeptAddReq;
import com.ytrue.bean.req.system.SysDeptUpdateReq;
import com.ytrue.bean.resp.system.SysDeptIdResp;
import com.ytrue.bean.resp.system.SysDeptListResp;
import com.ytrue.infra.db.base.IBaseService;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysDeptService
 * @date 2022/12/7 11:45
 */
public interface SysDeptService extends IBaseService<SysDept> {


    /**
     * 查询
     * @param queryParam
     * @return
     */
    List<SysDeptListResp> listBySysDeptListQuery(SysDeptListQuery queryParam);

    /**
     * 保存部门x
     *
     * @param requestParam
     */
    void addSysDept(SysDeptAddReq requestParam);


    /**
     * 更新部门
     *
     * @param requestParam
     */
    void updateSysDept(SysDeptUpdateReq requestParam);

    /**
     * 批量删除部门
     *
     * @param ids
     */
    void removeBatchBySysDeptIds(List<Long> ids);

    /**
     * 根据数据范围获取部门id
     *
     * @return
     */
    Set<Long> listCurrentAccountDeptId();

    /**
     * 获取部门
     *
     * @param id
     * @return
     */
    SysDeptIdResp getBySysDeptId(Long id);


}
