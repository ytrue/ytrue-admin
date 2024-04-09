package com.ytrue.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.query.system.SysUserPageQuery;
import com.ytrue.bean.req.system.SysUserAddReq;
import com.ytrue.bean.req.system.SysUserUpdatePasswordReq;
import com.ytrue.bean.req.system.SysUserUpdateReq;
import com.ytrue.bean.resp.system.SysUserIdResp;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.tools.query.entity.QueryEntity;

import java.util.List;

/**
 * @author ytrue
 * @description: SysUserService
 * @date 2022/12/7 16:57
 */
public interface SysUserService extends IBaseService<SysUser> {

    /**
     * 查询
     * @param queryParam
     * @return
     */
    IPage<SysUserListResp> listBySysUserPageQuery(SysUserPageQuery queryParam);




    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUserIdResp getBySysUserId(Long id);

    /**
     * 新增用户
     *
     * @param requestParam
     */
    void addSysUser(SysUserAddReq requestParam);

    /**
     * 修改用户
     *
     * @param requestParam
     */
    void updateSysUser(SysUserUpdateReq requestParam);

    /**
     * 删除用户
     *
     * @param ids
     */
    void removeBatchUserByIds(List<Long> ids);


    /**
     * 修改当前用户密码
     *
     * @param requestParam
     */
    void updateSysUserPassword(SysUserUpdatePasswordReq requestParam);

    /**
     * 重置密码
     *
     * @param userId
     */
    void resetSysUserPassword(Long userId);


}
