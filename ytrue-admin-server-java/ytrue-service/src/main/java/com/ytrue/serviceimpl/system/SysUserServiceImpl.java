package com.ytrue.serviceimpl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.dataobject.system.SysUserJob;
import com.ytrue.bean.dataobject.system.SysUserRole;
import com.ytrue.bean.query.system.SysUserPageQuery;
import com.ytrue.bean.req.system.SysUserAddReq;
import com.ytrue.bean.req.system.SysUserUpdatePasswordReq;
import com.ytrue.bean.req.system.SysUserUpdateReq;
import com.ytrue.bean.resp.system.SysUserIdResp;
import com.ytrue.bean.resp.system.SysUserListResp;
import com.ytrue.repository.mysql.system.SysUserDao;
import com.ytrue.repository.mysql.system.SysUserJobDao;
import com.ytrue.repository.mysql.system.SysUserRoleDao;
import com.ytrue.infra.core.constant.StrPool;
import com.ytrue.infra.core.response.ServerResponseInfo;
import com.ytrue.infra.core.response.ServerResponseInfoEnum;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.mysql.base.BaseServiceImpl;
import com.ytrue.infra.mysql.query.entity.QueryDefinition;
import com.ytrue.infra.mysql.query.enums.QueryMethod;
import com.ytrue.infra.mysql.query.util.QueryHelp;
import com.ytrue.infra.security.service.LoginService;
import com.ytrue.infra.security.util.SecurityUtil;
import com.ytrue.manager.DataScopeManager;
import com.ytrue.service.system.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequiredArgsConstructor
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUser> implements SysUserService {


    private final SysUserRoleDao sysUserRoleDao;
    private final SysUserJobDao sysUserJobDao;
    private final PasswordEncoder passwordEncoder;
    private final DataScopeManager dataScopeManager;
    private final LoginService loginService;

    @Override
    public IPage<SysUserListResp> listBySysUserPageQuery(SysUserPageQuery queryParam) {
        // 处理数据过滤
        Set<Long> deptIds = dataScopeManager.listDeptIdDataScope();
        // 生成 QueryDefinition
        QueryDefinition queryDefinition = QueryHelp.generateQueryDefinition(queryParam);
        // 判断是不是仅本人数据权限
        if (!deptIds.contains(0L)) {
            if (CollUtil.isNotEmpty(deptIds)) {
                queryDefinition.where(SysUser::getDeptId, QueryMethod.in, deptIds.stream().toList(), "u");
            }
        } else {
            queryDefinition.where(SysUser::getId, QueryMethod.eq, SecurityUtil.getLoginUser().getUser().getUserId(), "u");
        }
        queryDefinition.orderBy(SysUser::getCreateTime, Boolean.FALSE);
        queryDefinition.orderBy(SysUser::getId, Boolean.FALSE);
        // 调用查询
        return baseMapper.selectWithDeptName(queryParam.page(), QueryHelp.buildQueryWrapper(queryDefinition));
    }

    @Override
    public SysUserIdResp getBySysUserId(Long id) {
        SysUser user = getById(id);
        AssertUtil.notNull(user, ServerResponseInfoEnum.NOT_FOUND);
        // 获取对应的岗位
        Set<Long> jobIds = sysUserJobDao.selectList(Wrappers.<SysUserJob>lambdaQuery().eq(SysUserJob::getUserId, id)).stream().map(SysUserJob::getJobId).collect(Collectors.toSet());
        // 获取对应的角色
        Set<Long> roleIds = sysUserRoleDao.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id)).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        SysUserIdResp result = BeanUtils.copyProperties(user, SysUserIdResp::new);
        result.setJobIds(jobIds);
        result.setRoleIds(roleIds);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSysUser(SysUserAddReq requestParam) {
        SysUser sysUser = BeanUtils.copyProperties(requestParam, SysUser::new);

        sysUser.setPassword(passwordEncoder.encode(StrPool.DEFAULT_PASSWORD));

        // 校验账号是否存在
        AssertUtil.isNull(lambdaQuery().eq(SysUser::getUsername, requestParam.getUsername()).one(), ServerResponseInfo.error("账号已存在"));
        // 保存用户
        save(sysUser);
        // 保存用户与部门,角色的关系
        saveRoleAndJobRelation(sysUser.getId(), requestParam.getJobIds(), requestParam.getRoleIds());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSysUser(SysUserUpdateReq requestParam) {
        SysUser sysUser = this.getById(requestParam.getId());
        AssertUtil.notNull(sysUser, ServerResponseInfoEnum.ILLEGAL_OPERATION);

        BeanUtils.copyProperties(requestParam, sysUser);

        // 删除用户与部门,角色的关系
        removeRoleAndJobRelation(Collections.singletonList(sysUser.getId()));
        // 更新角色
        updateById(sysUser);
        // 保存用户与菜单,部门的关系
        saveRoleAndJobRelation(sysUser.getId(), requestParam.getJobIds(), requestParam.getRoleIds());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBySysUserIds(List<Long> ids) {
        // 删除用户
        removeBatchByIds(ids);
        // 删除用户与部门,角色的关系
        removeRoleAndJobRelation(ids);
    }

    @Override
    public void updateSysUserPassword(SysUserUpdatePasswordReq requestParam) {
        String userId = SecurityUtil.getLoginUser().getUser().getUserId();

        SysUser sysUser = this.getById(Long.valueOf(userId));

        AssertUtil.isTrue(passwordEncoder.matches(requestParam.getOldPassword(), sysUser.getPassword()), ServerResponseInfo.error("旧密码错误"));
        AssertUtil.objectNotEquals(requestParam.getOldPassword(), requestParam.getNewPassword(), ServerResponseInfo.error("新密码不能与旧密码相同"));

        sysUser.setPassword(passwordEncoder.encode(requestParam.getNewPassword()));
        this.updateById(sysUser);
        // 修改完成密码清除登录
        loginService.logout(userId);
    }

    @Override
    public void resetSysUserPassword(Long userId) {
        this.lambdaUpdate().eq(SysUser::getId, userId).set(SysUser::getPassword, passwordEncoder.encode(StrPool.DEFAULT_PASSWORD)).update();
    }

    /**
     * 保存用户与部门,角色的关系
     *
     * @param userIds
     * @param jobIds
     * @param roleIds
     */
    private void saveRoleAndJobRelation(Long userIds, Set<Long> jobIds, Set<Long> roleIds) {
        //保存角色与菜单关系
        if (CollUtil.isNotEmpty(jobIds)) {
            sysUserJobDao.insertBatchUserJob(userIds, jobIds);
        }

        //保存角色与部门关系
        if (CollUtil.isNotEmpty(roleIds)) {
            sysUserRoleDao.insertBatchUserRole(userIds, roleIds);
        }
    }

    /**
     * 删除用户与部门,角色的关系
     *
     * @param userIds
     */
    private void removeRoleAndJobRelation(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        //先删除用户与岗位关系
        sysUserJobDao.delete(Wrappers.<SysUserJob>lambdaQuery().in(SysUserJob::getUserId, userIds));
        //先删除用户与角色关系
        sysUserRoleDao.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, userIds));
    }
}
