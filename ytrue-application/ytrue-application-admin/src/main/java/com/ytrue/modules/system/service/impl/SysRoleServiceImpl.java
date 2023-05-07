package com.ytrue.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.db.mybatis.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.common.utils.BeanUtils;
import com.ytrue.modules.system.dao.*;
import com.ytrue.modules.system.model.req.SysRoleReq;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.po.SysRoleDept;
import com.ytrue.modules.system.model.po.SysRoleMenu;
import com.ytrue.modules.system.service.ISysRoleService;
import com.ytrue.modules.system.service.manager.DataScopeManager;
import com.ytrue.tools.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final SysRoleDao sysRoleDao;

    private final DataScopeManager dataScopeManager;


    @Override
    public Set<Long> getRoleIdsByDataScope() {
        // 处理下数据过滤
        Set<Long> deptIds = dataScopeManager.handleDataScope();
        Set<Long> roleIds = new HashSet<>();
        // 如果包含0呢
        if (CollectionUtil.isNotEmpty(deptIds)) {
            if (!deptIds.contains(0L)) {
                roleIds = sysRoleDeptDao.selectList(Wrappers.<SysRoleDept>lambdaQuery().in(SysRoleDept::getDeptId, deptIds)).stream().map(SysRoleDept::getRoleId).collect(Collectors.toSet());
                // 要把当前账号的角色放入进去，如果级别包含本人
                String userId = SecurityUtils.getLoginUser().getUser().getUserId();
                roleIds.addAll(listByUserId(Convert.toLong(userId)).stream().map(SysRole::getId).collect(Collectors.toSet()));
            } else {
                roleIds = deptIds;
            }
        }
        return roleIds;
    }


    @Override
    public SysRoleReq getRoleById(Long id) {
        // 获取角色
        SysRole role = getById(id);
        AssertUtils.notNull(role, ResponseCode.DATA_NOT_FOUND);
        // 获取对应的菜单
        Set<Long> menuIds = sysMenuDao.listMenuIdByRoleId(id, role.getMenuCheckStrictly());
        // 获取对应的部门
        Set<Long> deptIds = sysDeptDao.listDeptIdByRoleId(id, role.getDeptCheckStrictly());

        SysRoleReq roleDTO = BeanUtils.cgLibCopyBean(role, SysRoleReq::new);
        roleDTO.setMenuIds(menuIds);
        roleDTO.setDeptIds(deptIds);
        return roleDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRole(SysRoleReq sysRoleReq) {
        // 转换下
        SysRole role = BeanUtils.cgLibCopyBean(sysRoleReq, SysRole::new);
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
        SysRole role = BeanUtils.cgLibCopyBean(sysRoleReq, SysRole::new);
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
        return sysRoleDao.listByUserId(userId);
    }

    /**
     * 保存角色与菜单,部门的关系
     *
     * @param sysRoleReq
     */
    private void saveMenuAndDeptRelation(SysRoleReq sysRoleReq) {
        //保存角色与菜单关系
        if (sysRoleReq.getMenuIds().size() > 0) {
            sysRoleMenuDao.insertBatchRoleMenu(sysRoleReq.getId(), sysRoleReq.getMenuIds());
        }
        if (sysRoleReq.getDeptIds().size() > 0) {
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
