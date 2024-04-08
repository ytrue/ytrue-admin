package com.ytrue.service.system;

import com.ytrue.bean.resp.system.SysRoleDetailResp;
import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.req.system.SysRoleReq;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleService
 * @date 2022/12/7 15:23
 */
public interface SysRoleService extends IBaseService<SysRole> {

    /**
     * 获取role信息根据id
     *
     * @param id
     * @return
     */
    SysRoleDetailResp getRoleById(Long id);

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
    void removeBatchRoleByIds(List<Long> ids);


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
    Set<Long> listCurrentAccountRoleId();
}
