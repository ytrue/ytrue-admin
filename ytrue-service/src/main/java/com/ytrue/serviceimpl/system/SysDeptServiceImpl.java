package com.ytrue.serviceimpl.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.dataobject.system.SysRoleDept;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.SysDeptDao;
import com.ytrue.infra.db.dao.system.SysRoleDeptDao;
import com.ytrue.infra.db.dao.system.SysUserDao;
import com.ytrue.manager.DataScopeManager;
import com.ytrue.service.system.SysDeptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysDeptServiceImpl
 * @date 2022/12/7 11:46
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDept> implements SysDeptService {

    private final SysUserDao sysUserDao;

    private final SysRoleDeptDao sysRoleDeptDao;

    private final DataScopeManager dataScopeManager;


    @Override
    public Set<Long> listCurrentAccountDeptId() {
        return dataScopeManager.listDeptIdDataScope();
    }

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
        AssertUtil.numberNotEquals(sysDept.getId(), sysDept.getPid(), ServerResponseCode.error("父级不能是自己"));

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
            AssertUtil.isNull(childDept, ServerResponseCode.error("存在子级，请解除后再试"));

            // 校验用户绑定
            SysUser sysUser = sysUserDao.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, id));
            AssertUtil.isNull(sysUser, ServerResponseCode.error("存在用户关联，请解除后再试"));

            // 校验角色绑定
            SysRoleDept sysRoleDept = sysRoleDeptDao.selectOne(Wrappers.<SysRoleDept>lambdaQuery().eq(SysRoleDept::getDeptId, id));
            AssertUtil.isNull(sysRoleDept, ServerResponseCode.error("存在角色关联，请解除后再试"));

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
