package com.ytrue.modules.system.service;

import com.ytrue.common.base.IBaseService;
import com.ytrue.modules.system.model.SysRole;
import com.ytrue.modules.system.model.dto.SysRoleDTO;

import java.util.List;

/**
 * @author ytrue
 * @description: ISysRoleService
 * @date 2022/12/7 15:23
 */
public interface ISysRoleService extends IBaseService<SysRole> {

    /**
     * 获取role信息根据id
     *
     * @param id
     * @return
     */
    SysRoleDTO getRoleById(Long id);

    /**
     * 新增角色
     *
     * @param sysRoleDTO
     */
    void addRole(SysRoleDTO sysRoleDTO);

    /**
     * 修改角色
     *
     * @param sysRoleDTO
     */
    void updateRole(SysRoleDTO sysRoleDTO);

    /**
     * 删除角色
     *
     * @param ids
     */
    void removeBatchRole(List<Long> ids);
}
