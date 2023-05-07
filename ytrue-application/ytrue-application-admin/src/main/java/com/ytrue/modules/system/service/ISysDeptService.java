package com.ytrue.modules.system.service;

import com.ytrue.db.mybatis.base.IBaseService;
import com.ytrue.modules.system.model.po.SysDept;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: ISysDeptService
 * @date 2022/12/7 11:45
 */
public interface ISysDeptService extends IBaseService<SysDept> {


    /**
     * 保存部门x
     *
     * @param sysDept
     */
    void addDept(SysDept sysDept);


    /**
     * 更新部门
     *
     * @param sysDept
     */
    void updateDept(SysDept sysDept);

    /**
     * 批量删除部门
     *
     * @param ids
     */
    void removeBatchDept(List<Long> ids);

    /**
     * 根据数据范围获取部门id
     *
     * @return
     */
    Set<Long> getDeptIdByDataScope();
}
