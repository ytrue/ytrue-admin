package com.ytrue.modules.system.service;

import com.ytrue.db.mybatis.base.IBaseService;
import com.ytrue.modules.system.model.req.SysRoleReq;
import com.ytrue.modules.system.model.po.SysRole;

import java.util.List;
import java.util.Set;

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
    SysRoleReq getRoleById(Long id);

    /**
     * 新增角色
     *
     * @param sysRoleReq
     */
    void addRole(SysRoleReq sysRoleReq);

    /**
     * 修改角色
     *
     * @param sysRoleReq
     */
    void updateRole(SysRoleReq sysRoleReq);

    /**
     * 删除角色
     *
     * @param ids
     */
    void removeBatchRole(List<Long> ids);


    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    Set<SysRole> listByUserId(Long userId);


    /**
     * 根据数据范围获取角色id
     *
     * @return
     */
    Set<Long> getRoleIdsByDataScope();
}
