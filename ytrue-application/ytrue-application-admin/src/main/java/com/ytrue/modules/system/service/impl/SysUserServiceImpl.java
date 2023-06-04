package com.ytrue.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.db.mybatis.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.common.utils.BeanUtils;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.dao.SysUserJobDao;
import com.ytrue.modules.system.dao.SysUserRoleDao;
import com.ytrue.modules.system.model.req.SysUserReq;
import com.ytrue.modules.system.model.res.SysUserDetailRes;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.po.SysUserJob;
import com.ytrue.modules.system.model.po.SysUserRole;
import com.ytrue.modules.system.model.res.SysUserListRes;
import com.ytrue.modules.system.service.ISysUserService;
import com.ytrue.modules.system.service.manager.DataScopeManager;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.enums.QueryMethod;
import com.ytrue.tools.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @description: SysUserServiceImpl
 * @date 2022/12/7 16:58
 */
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUser> implements ISysUserService {


    private final SysUserRoleDao sysUserRoleDao;

    private final SysUserJobDao sysUserJobDao;

    private final SysUserDao sysUserDao;

    private final DataScopeManager dataScopeManager;

    @Override
    public IPage<SysUserListRes> paginate(IPage<SysUserListRes> page, QueryEntity query) {
        // 处理数据过滤
        Set<Long> deptIds = dataScopeManager.listDeptIdDataScope();
        if (!deptIds.contains(0L)) {
            query.addFilter(SysUser::getDeptId, QueryMethod.in, Convert.toList(deptIds), "u");
        } else {
            query.addFilter(SysUser::getId, QueryMethod.eq, SecurityUtils.getLoginUser().getUser().getUserId(), "u");
        }
        return sysUserDao.listWithDeptName(page, query);
    }

    @Override
    public SysUserDetailRes getUserById(Long id) {
        SysUser user = getById(id);
        AssertUtils.notNull(user, ResponseCode.DATA_NOT_FOUND);
        // 获取对应的岗位
        Set<Long> jobIds = sysUserJobDao.selectList(Wrappers.<SysUserJob>lambdaQuery().eq(SysUserJob::getUserId, id)).stream().map(SysUserJob::getJobId).collect(Collectors.toSet());
        // 获取对应的角色
        Set<Long> roleIds = sysUserRoleDao.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id)).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        SysUserDetailRes result = BeanUtils.cgLibCopyBean(user, SysUserDetailRes::new);
        result.setJobIds(jobIds);
        result.setRoleIds(roleIds);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(SysUserReq sysUserReq) {
        SysUser user = BeanUtils.cgLibCopyBean(sysUserReq, SysUser::new);
        // 校验账号是否存在
        AssertUtils.isNull(lambdaQuery().eq(SysUser::getUsername, sysUserReq.getUsername()).one(), ResponseCode.ACCOUNT_EXISTS);
        // 保存用户
        save(user);
        // 保存用户与部门,角色的关系
        sysUserReq.setId(user.getId());
        saveRoleAndJobRelation(sysUserReq);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(SysUserReq sysUserReq) {
        SysUser user = BeanUtils.cgLibCopyBean(sysUserReq, SysUser::new);
        // 删除用户与部门,角色的关系
        deleteRoleAndJobRelation(Collections.singletonList(sysUserReq.getId()));
        // 更新角色
        updateById(user);
        // 保存用户与菜单,部门的关系
        saveRoleAndJobRelation(sysUserReq);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBatchUser(List<Long> ids) {
        // 删除用户
        removeBatchByIds(ids);
        // 删除用户与部门,角色的关系
        deleteRoleAndJobRelation(ids);
    }

    /**
     * 保存用户与部门,角色的关系
     *
     * @param sysUserReq
     */
    private void saveRoleAndJobRelation(SysUserReq sysUserReq) {
        //保存用户与岗位关系
        if (sysUserReq.getJobIds().size() > 0) {
            sysUserJobDao.insertBatchUserJob(sysUserReq.getId(), sysUserReq.getJobIds());
        }
        //保存户与角色关系
        if (sysUserReq.getRoleIds().size() > 0) {
            sysUserRoleDao.insertBatchUserRole(sysUserReq.getId(), sysUserReq.getRoleIds());
        }
    }

    /**
     * 删除用户与部门,角色的关系
     *
     * @param userIds
     */
    private void deleteRoleAndJobRelation(List<Long> userIds) {
        //先删除用户与岗位关系
        sysUserJobDao.delete(Wrappers.<SysUserJob>lambdaQuery().in(SysUserJob::getUserId, userIds));
        //先删除用户与角色关系
        sysUserRoleDao.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, userIds));
    }

    @Override
    public SysUser getUserByUsername(String username) {
        SysUser user = lambdaQuery().eq(SysUser::getUsername, username).one();
        AssertUtils.notNull(user, ResponseCode.DATA_NOT_FOUND);
        return user;
    }

}
