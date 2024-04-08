package com.ytrue.serviceimpl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.dataobject.system.SysRoleDept;
import com.ytrue.bean.dataobject.system.SysRoleMenu;
import com.ytrue.bean.dataobject.system.SysUserRole;
import com.ytrue.bean.req.system.SysRoleAddReq;
import com.ytrue.bean.req.system.SysRoleUpdateReq;
import com.ytrue.bean.resp.system.SysRoleDetailResp;
import com.ytrue.infra.core.response.ResponseCodeEnum;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtil;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.*;
import com.ytrue.manager.DataScopeManager;
import com.ytrue.service.system.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleServiceImpl
 * @date 2022/12/7 15:23
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRole> implements SysRoleService {


    private final SysRoleDeptDao sysRoleDeptDao;
    private final SysRoleMenuDao sysRoleMenuDao;
    private final SysUserRoleDao sysUserRoleDao;
    private final SysDeptDao sysDeptDao;
    private final SysMenuDao sysMenuDao;
    private final DataScopeManager dataScopeManager;


    @Override
    public Set<Long> listCurrentAccountRoleId() {
        return dataScopeManager.listRoleIdDataScope();
    }


    @Override
    public SysRoleDetailResp getRoleById(Long id) {
        // 获取角色
        SysRole role = getById(id);
        AssertUtil.notNull(role, ResponseCodeEnum.DATA_NOT_FOUND);
        // 获取对应的菜单
        Set<Long> menuIds = sysMenuDao.selectMenuIdsBySysRoleId(id, role.getMenuCheckStrictly());
        // 获取对应的部门
        Set<Long> deptIds = sysDeptDao.selectDeptIdsBySysRoleId(id, role.getDeptCheckStrictly());

        SysRoleDetailResp roleDTO = BeanUtil.cgLibCopyBean(role, SysRoleDetailResp::new);
        roleDTO.setMenuIds(menuIds);
        roleDTO.setDeptIds(deptIds);
        return roleDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSysRole(SysRoleAddReq requestParam) {
        // 转换下
        SysRole role = BeanUtil.cgLibCopyBean(requestParam, SysRole::new);
        // 更新角色
        save(role);
        // 保存角色与菜单,部门的关系
        saveMenuAndDeptRelation(role.getId(), requestParam.getMenuIds(), requestParam.getDeptIds());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSysRole(SysRoleUpdateReq requestParam) {
        SysRole sysRole = this.getById(requestParam.getId());
        AssertUtil.notNull(sysRole,ResponseCodeEnum.ILLEGAL_OPERATION);

        // 转换下
        SysRole role = BeanUtil.cgLibCopyBean(requestParam, SysRole::new);
        // 删除角色与菜单,部门的关系
        removeMenuAndDeptRelation(Collections.singletonList(requestParam.getId()));
        // 更新角色
        updateById(role);
        // 保存角色与菜单,部门的关系
        saveMenuAndDeptRelation(role.getId(), requestParam.getMenuIds(), requestParam.getDeptIds());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBatchSysRoleByIds(List<Long> ids) {
        // 校验集合
        AssertUtil.collectionIsNotEmpty(ids, ResponseCodeEnum.ILLEGAL_OPERATION);

        // 只要存在一个 就是无法删除的
        SysUserRole sysUserRole = sysUserRoleDao.selectOne(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getRoleId, ids));
        AssertUtil.isNull(sysUserRole, ServerResponseCode.error("存在用户关联，请解除后再试"));

        // 删除角色
        removeBatchByIds(ids);
        // 删除角色与菜单,部门的关系
        removeMenuAndDeptRelation(ids);
    }

    @Override
    public Set<SysRole> listBySysUserId(Long userId) {
        return baseMapper.selectBySysUserId(userId);
    }

    /**
     * 保存角色与菜单,部门的关系
     *
     * @param roleId
     * @param menuIds
     * @param deptIds
     */
    private void saveMenuAndDeptRelation(Long roleId, Set<Long> menuIds, Set<Long> deptIds) {
        //保存角色与菜单关系
        if (CollUtil.isNotEmpty(menuIds)) {
            sysRoleMenuDao.insertBatchRoleMenu(roleId, menuIds);
        }

        //保存角色与部门关系
        if (CollUtil.isNotEmpty(deptIds)) {
            sysRoleDeptDao.insertBatchRoleDept(roleId, deptIds);
        }
    }

    /**
     * 删除角色与菜单,部门的关系
     *
     * @param roleIds
     */
    private void removeMenuAndDeptRelation(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)){
            return;
        }
        //先删除角色与菜单关系
        sysRoleMenuDao.delete(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds));
        //先删除角色与部门关系
        sysRoleDeptDao.delete(Wrappers.<SysRoleDept>lambdaQuery().in(SysRoleDept::getRoleId, roleIds));
    }
}
