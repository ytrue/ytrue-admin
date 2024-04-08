package com.ytrue.service.system;

import com.ytrue.bean.req.system.SysRoleAddReq;
import com.ytrue.bean.resp.system.SysRoleDetailResp;
import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.req.system.SysRoleUpdateReq;

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
     * @param requestParam
     */
    void addSysRole(SysRoleAddReq requestParam);

    /**
     * 修改角色
     *
     * @param requestParam
     */
    void updateSysRole(SysRoleUpdateReq requestParam);

    /**
     * 删除角色
     *
     * @param ids
     */
    void removeBatchSysRoleByIds(List<Long> ids);


    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    Set<SysRole> listBySysUserId(Long userId);


    /**
     * 根据数据范围获取角色id
     *
     * @return
     */
    Set<Long> listCurrentAccountRoleId();
}
