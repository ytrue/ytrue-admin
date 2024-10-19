package com.ytrue.serviceimpl.system;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.dataobject.system.SysRoleDept;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.query.system.SysDeptListQuery;
import com.ytrue.bean.req.system.SysDeptAddReq;
import com.ytrue.bean.req.system.SysDeptUpdateReq;
import com.ytrue.bean.resp.system.SysDeptIdResp;
import com.ytrue.bean.resp.system.SysDeptListResp;
import com.ytrue.infra.core.response.ServerResponseInfoEnum;
import com.ytrue.infra.core.response.ServerResponseInfo;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.mysql.base.BaseServiceImpl;
import com.ytrue.repository.mysql.system.SysDeptDao;
import com.ytrue.repository.mysql.system.SysRoleDeptDao;
import com.ytrue.repository.mysql.system.SysUserDao;
import com.ytrue.manager.DataScopeManager;
import com.ytrue.service.system.SysDeptService;
import com.ytrue.infra.mysql.query.util.QueryHelp;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDept> implements SysDeptService {

    private final SysUserDao sysUserDao;
    private final SysRoleDeptDao sysRoleDeptDao;
    private final DataScopeManager dataScopeManager;


    @Override
    public Set<Long> listCurrentAccountDeptId() {
        return dataScopeManager.listDeptIdDataScope();
    }

    @Override
    public SysDeptIdResp getBySysDeptId(Long id) {
        SysDept sysDept = getById(id);
        AssertUtil.notNull(sysDept, ServerResponseInfoEnum.NOT_FOUND);

        return BeanUtils.copyProperties(sysDept, SysDeptIdResp::new);
    }


    @Override
    public List<SysDeptListResp> listBySysDeptListQuery(SysDeptListQuery queryParam) {
        // 数据范围限制
        Set<Long> deptIds = this.listCurrentAccountDeptId();
        // 构建查询条件
        LambdaQueryWrapper<SysDept> queryWrapper = QueryHelp.<SysDept>builderlambdaQueryWrapper(queryParam)
                .in(CollectionUtil.isNotEmpty(deptIds), SysDept::getId, deptIds)
                .orderByAsc(SysDept::getDeptSort)
                .orderByDesc(SysDept::getId);

        List<SysDept> list = this.list(queryWrapper);

        // 转换输出
        return BeanUtils.copyListProperties(list, SysDeptListResp::new);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSysDept(SysDeptAddReq requestParam) {
        // 转换一下
        SysDept sysDept = BeanUtils.copyProperties(requestParam, SysDept::new);
        // 保存
        this.save(sysDept);
        // 更新数量
        updateSubCnt(sysDept.getPid());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSysDept(SysDeptUpdateReq requestParam) {
        // 获取id的部门
        SysDept sysDept = getById(requestParam.getId());
        AssertUtil.notNull(sysDept, ServerResponseInfoEnum.ILLEGAL_OPERATION);

        // 校验父级不能是自己
        AssertUtil.numberNotEquals(requestParam.getId(), requestParam.getPid(), ServerResponseInfo.error("父级不能是自己"));

        // 获取id
        Long oldPid = sysDept.getPid();
        Long newPid = requestParam.getPid();

        // bean转换
        BeanUtils.copyProperties(requestParam, sysDept);

        updateById(sysDept);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBySysDeptIds(List<Long> ids) {
        // 校验集合
        AssertUtil.collectionIsNotEmpty(ids, ServerResponseInfoEnum.ILLEGAL_OPERATION);

        // 循环校验
        for (Long id : ids) {
            SysDept childDept = lambdaQuery().eq(SysDept::getPid, id).one();
            AssertUtil.isNull(childDept, ServerResponseInfo.error("存在子级，请解除后再试"));

            // 校验用户绑定
            SysUser sysUser = sysUserDao.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, id));
            AssertUtil.isNull(sysUser, ServerResponseInfo.error("存在用户关联，请解除后再试"));

            // 校验角色绑定
            SysRoleDept sysRoleDept = sysRoleDeptDao.selectOne(Wrappers.<SysRoleDept>lambdaQuery().eq(SysRoleDept::getDeptId, id));
            AssertUtil.isNull(sysRoleDept, ServerResponseInfo.error("存在角色关联，请解除后再试"));
        }

        // 操作删除
        this.doRemoveBatchBySysDeptIds(ids);
    }


    private void doRemoveBatchBySysDeptIds(List<Long> ids) {
        // 处理
        for (Long id : ids) {
            SysDept dept = getById(id);
            removeById(id);
            // 更新节点的数量
            updateSubCnt(dept.getPid());
        }
    }

    /**
     * 更新数量
     *
     * @param deptId
     */
    private void updateSubCnt(long deptId) {
        Long count = lambdaQuery().eq(SysDept::getPid, deptId).count();
        // 更新
        lambdaUpdate().eq(SysDept::getId, deptId).set(SysDept::getSubCount, count).update();
    }

}
