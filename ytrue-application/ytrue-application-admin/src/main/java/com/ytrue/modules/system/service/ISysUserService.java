package com.ytrue.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.base.IBaseService;
import com.ytrue.modules.system.model.res.SysUserDetailRes;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.req.SysUserReq;
import com.ytrue.modules.system.model.res.SysUserListRes;
import com.ytrue.tools.query.entity.QueryEntity;

import java.util.List;

/**
 * @author ytrue
 * @description: ISysUserService
 * @date 2022/12/7 16:57
 */
public interface ISysUserService extends IBaseService<SysUser> {


    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<SysUserListRes> paginate(IPage<SysUserListRes> page, QueryEntity query);


    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUserDetailRes getUserById(Long id);

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
