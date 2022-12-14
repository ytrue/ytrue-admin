package com.ytrue.modules.system.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.base.IBaseDao;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.vo.SysUserListVO;
import com.ytrue.tools.query.entity.QueryEntity;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserDao
 * @date 2022/12/7 16:57
 */
public interface SysUserDao extends IBaseDao<SysUser> {


    /**
     * 查询用户的所有权限
     *
     * @param userId
     * @return
     */
    Set<String> listPermsByUserId(Long userId);


    /**
     * 列表查询
     *
     * @param page
     * @param queryEntity
     * @return
     */
    IPage<SysUserListVO> listWithDeptName(IPage<SysUserListVO> page, QueryEntity queryEntity);
}
