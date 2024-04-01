package com.ytrue.serviceimpl.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.dataobject.system.SysRoleDept;
import com.ytrue.bean.dataobject.system.SysRoleMenu;
import com.ytrue.bean.req.system.SysRoleReq;
import com.ytrue.bean.resp.system.SysRoleDetailResp;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtil;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.*;
import com.ytrue.manager.DataScopeManager;
import com.ytrue.service.system.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import  com.ytrue.infra.core.response.ResponseCodeEnum;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleServiceImpl
 * @date 2022/12/7 15:23
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRole> implements SysRoleService {


    private final SysRoleDeptDao sysRoleDeptDao;

    private final SysRoleMenuDao sysRoleMenuDao;

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
        Set<Long> menuIds = sysMenuDao.listMenuIdByRoleId(id, role.getMenuCheckStrictly());
        // 获取对应的部门
        Set<Long> deptIds = sysDeptDao.listDeptIdByRoleId(id, role.getDeptCheckStrictly());

        SysRoleDetailResp roleDTO = BeanUtil.cgLibCopyBean(role, SysRoleDetailResp::new);
        roleDTO.setMenuIds(menuIds);
        roleDTO.setDeptIds(deptIds);
        return roleDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRole(SysRoleReq sysRoleReq) {
        // 转换下
        SysRole role = BeanUtil.cgLibCopyBean(sysRoleReq, SysRole::new);
        // 更新角色
        save(role);
        // 保存角色与菜单,部门的关系
        sysRoleReq.setId(role.getId());
        saveMenuAndDeptRelation(sysRoleReq);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(SysRoleReq sysRoleReq) {
        // 转换下
        SysRole role = BeanUtil.cgLibCopyBean(sysRoleReq, SysRole::new);
        // 删除角色与菜单,部门的关系
        deleteMenuAndDeptRelation(Collections.singletonList(sysRoleReq.getId()));
        // 更新角色
        updateById(role);
        // 保存角色与菜单,部门的关系
        saveMenuAndDeptRelation(sysRoleReq);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBatchRole(List<Long> ids) {
        // 删除角色
        removeBatchByIds(ids);
        // 删除角色与菜单,部门的关系
        deleteMenuAndDeptRelation(ids);
    }

    @Override
    public Set<SysRole> listByUserId(Long userId) {
        return baseMapper.listByUserId(userId);
    }

    /**
     * 保存角色与菜单,部门的关系
     *
     * @param sysRoleReq
     */
    private void saveMenuAndDeptRelation(SysRoleReq sysRoleReq) {
        //保存角色与菜单关系
        if (!sysRoleReq.getMenuIds().isEmpty()) {
            sysRoleMenuDao.insertBatchRoleMenu(sysRoleReq.getId(), sysRoleReq.getMenuIds());
        }
        if (!sysRoleReq.getDeptIds().isEmpty()) {
            //保存角色与部门关系
            sysRoleDeptDao.insertBatchRoleDept(sysRoleReq.getId(), sysRoleReq.getDeptIds());
        }
    }

    /**
     * 删除角色与菜单,部门的关系
     *
     * @param roleIds
     */
    private void deleteMenuAndDeptRelation(List<Long> roleIds) {
        //先删除角色与菜单关系
        sysRoleMenuDao.delete(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds));
        //先删除角色与部门关系
        sysRoleDeptDao.delete(Wrappers.<SysRoleDept>lambdaQuery().in(SysRoleDept::getRoleId, roleIds));
    }
}
