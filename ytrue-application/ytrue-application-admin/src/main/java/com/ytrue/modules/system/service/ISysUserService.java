package com.ytrue.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.base.IBaseService;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.dto.SysUserDTO;
import com.ytrue.modules.system.model.vo.SysUserListVO;
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
    IPage<SysUserListVO> paginate(IPage<SysUserListVO> page, QueryEntity query);


    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUserDTO getUserById(Long id);

    /**
     * 新增用户
     *
     * @param sysUserDTO
     */
    void addUser(SysUserDTO sysUserDTO);

    /**
     * 修改用户
     *
     * @param sysUserDTO
     */
    void updateUser(SysUserDTO sysUserDTO);

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
