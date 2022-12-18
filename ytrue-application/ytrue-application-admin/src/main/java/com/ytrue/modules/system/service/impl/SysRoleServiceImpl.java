package com.ytrue.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.common.utils.BeanUtils;
import com.ytrue.modules.system.dao.*;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.po.SysRoleDept;
import com.ytrue.modules.system.model.po.SysRoleMenu;
import com.ytrue.modules.system.model.dto.SysRoleDTO;
import com.ytrue.modules.system.service.ISysRoleService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRole> implements ISysRoleService {


    private final SysRoleDeptDao sysRoleDeptDao;

    private final SysRoleMenuDao sysRoleMenuDao;

    private final SysDeptDao sysDeptDao;

    private final SysMenuDao sysMenuDao;


    @Override
    public SysRoleDTO getRoleById(Long id) {
        // 获取角色
        SysRole role = getById(id);
        AssertUtils.notNull(role, ResponseCode.DATA_NOT_FOUND);
        // 获取对应的菜单
        Set<Long> menuIds = sysMenuDao.listMenuIdByRoleId(id, role.getMenuCheckStrictly());
        // 获取对应的部门
        Set<Long> deptIds = sysDeptDao.listDeptIdByRoleId(id, role.getDeptCheckStrictly());

        SysRoleDTO roleDTO = BeanUtils.cgLibCopyBean(role, SysRoleDTO::new);
        roleDTO.setMenuIds(menuIds);
        roleDTO.setDeptIds(deptIds);
        return roleDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRole(SysRoleDTO sysRoleDTO) {
        // 转换下
        SysRole role = BeanUtils.cgLibCopyBean(sysRoleDTO, SysRole::new);
        // 更新角色
        save(role);
        // 保存角色与菜单,部门的关系
        sysRoleDTO.setId(role.getId());
        saveMenuAndDeptRelation(sysRoleDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(SysRoleDTO sysRoleDTO) {
        // 转换下
        SysRole role = BeanUtils.cgLibCopyBean(sysRoleDTO, SysRole::new);
        // 删除角色与菜单,部门的关系
        deleteMenuAndDeptRelation(Collections.singletonList(sysRoleDTO.getId()));
        // 更新角色
        updateById(role);
        // 保存角色与菜单,部门的关系
        saveMenuAndDeptRelation(sysRoleDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBatchRole(List<Long> ids) {
        // 删除角色
        removeBatchByIds(ids);
        // 删除角色与菜单,部门的关系
        deleteMenuAndDeptRelation(ids);
    }

    /**
     * 保存角色与菜单,部门的关系
     *
     * @param sysRoleDTO
     */
    private void saveMenuAndDeptRelation(SysRoleDTO sysRoleDTO) {
        //保存角色与菜单关系
        if (sysRoleDTO.getMenuIds().size() > 0) {
            sysRoleMenuDao.insertBatchRoleMenu(sysRoleDTO.getId(), sysRoleDTO.getMenuIds());
        }
        if (sysRoleDTO.getDeptIds().size() > 0) {
            //保存角色与部门关系
            sysRoleDeptDao.insertBatchRoleDept(sysRoleDTO.getId(), sysRoleDTO.getDeptIds());
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
