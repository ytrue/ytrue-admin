package com.ytrue.modules.system.service.impl;

import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.dao.SysDeptDao;
import com.ytrue.modules.system.model.SysDept;
import com.ytrue.modules.system.service.ISysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ytrue
 * @description: SysDeptServiceImpl
 * @date 2022/12/7 11:46
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDept> implements ISysDeptService {

    /**
     * 保存部门
     *
     * @param sysDept
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDept(SysDept sysDept) {
        save(sysDept);
        updateSubCnt(sysDept.getPid());
    }

    /**
     * 更新部门
     *
     * @param sysDept
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDept(SysDept sysDept) {
        // 校验父级不能是自己
        AssertUtils.noteEquals(sysDept.getId(), sysDept.getPid(), ResponseCode.PARENT_EQ_ITSELF);

        // 旧的部门
        Long oldPid = getById(sysDept.getId()).getPid();
        Long newPid = sysDept.getPid();

        updateById(sysDept);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }

    @Override
    public void removeBatchDept(List<Long> ids) {
        for (Long id : ids) {
            SysDept childDept = lambdaQuery().eq(SysDept::getPid, id).one();
            AssertUtils.isNull(childDept, ResponseCode.HAS_CHILD);

            // 校验用户绑定 TODO

            // 校验角色绑定 TODO

            SysDept dept = getById(id);
            removeById(id);
            updateSubCnt(dept.getPid());
        }
    }


    /**
     * 更新数量
     *
     * @param deptId
     */
    private void updateSubCnt(Long deptId) {
        if (deptId == null) {
            return;
        }
        Long count = lambdaQuery().eq(SysDept::getPid, deptId).count();
        // 更新
        lambdaUpdate().eq(SysDept::getId, deptId).set(SysDept::getSubCount, count).update();
    }

}