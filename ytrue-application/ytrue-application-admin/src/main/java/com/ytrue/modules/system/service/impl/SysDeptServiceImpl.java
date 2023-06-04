package com.ytrue.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.db.mybatis.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.dao.SysDeptDao;
import com.ytrue.modules.system.dao.SysRoleDeptDao;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.model.po.SysRoleDept;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.service.ISysDeptService;
import com.ytrue.modules.system.service.manager.DataScopeManager;
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
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDept> implements ISysDeptService {

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

            // 校验用户绑定
            SysUser sysUser = sysUserDao.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, id));
            AssertUtils.isNull(sysUser, ResponseCode.HAS_USER_ASSOCIATION);

            // 校验角色绑定
            SysRoleDept sysRoleDept = sysRoleDeptDao.selectOne(Wrappers.<SysRoleDept>lambdaQuery().eq(SysRoleDept::getDeptId, id));
            AssertUtils.isNull(sysRoleDept, ResponseCode.HAS_ROLE_ASSOCIATION);

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
