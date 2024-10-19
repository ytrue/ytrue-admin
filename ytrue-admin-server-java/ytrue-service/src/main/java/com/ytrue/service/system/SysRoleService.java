package com.ytrue.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.query.system.SysRolePageQuery;
import com.ytrue.bean.req.system.SysRoleAddReq;
import com.ytrue.bean.req.system.SysRoleUpdateReq;

import com.ytrue.bean.resp.system.SysRoleIdResp;
import com.ytrue.bean.resp.system.SysRoleListResp;
import com.ytrue.infra.mysql.base.IBaseService;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleService
 * @date 2022/12/7 15:23
 */
public interface SysRoleService extends IBaseService<SysRole> {

    /**
     * 查询
     *
     * @param queryParam
     */
    IPage<SysRoleListResp> listBySysRolePageQuery(SysRolePageQuery queryParam);

    /**
     * 获取role信息根据id
     *
     * @param id
     * @return
     */
    SysRoleIdResp getBySysRoleId(Long id);

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
    void removeBySysRoleIds(List<Long> ids);


    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    List<SysRole> listBySysUserId(Long userId);


    /**
     * 根据数据范围获取角色id
     *
     * @return
     */
    Set<Long> listCurrentAccountRoleId();


}
