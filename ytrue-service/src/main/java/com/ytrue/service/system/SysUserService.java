package com.ytrue.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.req.system.SysUserReq;
import com.ytrue.bean.resp.system.SysUserDetailResp;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.tools.query.entity.QueryEntity;

import java.util.List;

/**
 * @author ytrue
 * @description: SysUserService
 * @date 2022/12/7 16:57
 */
public interface SysUserService extends IBaseService<SysUser> {


    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<SysUserListResp> paginate(IPage<SysUserListResp> page, QueryEntity query);


    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUserDetailResp getUserById(Long id);

    /**
     * 新增用户
     *
     * @param sysUserReq
     */
    void addUser(SysUserReq sysUserReq);

    /**
     * 修改用户
     *
     * @param sysUserReq
     */
    void updateUser(SysUserReq sysUserReq);

    /**
     * 删除用户
     *
     * @param ids
     */
    void removeBatchUser(List<Long> ids);

    /**
     * 根据用户名获取
     *
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);
}
