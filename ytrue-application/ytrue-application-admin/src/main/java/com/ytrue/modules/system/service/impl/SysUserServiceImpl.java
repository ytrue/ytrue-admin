package com.ytrue.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.common.utils.BeanUtils;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.dao.SysUserJobDao;
import com.ytrue.modules.system.dao.SysUserRoleDao;
import com.ytrue.modules.system.model.SysUser;
import com.ytrue.modules.system.model.SysUserJob;
import com.ytrue.modules.system.model.SysUserRole;
import com.ytrue.modules.system.model.dto.SysUserDTO;
import com.ytrue.modules.system.service.ISysUserService;
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

    @Override
    public SysUserDTO getUserById(Long id) {
        SysUser user = getById(id);
        AssertUtils.notNull(user, ResponseCode.DATA_NOT_FOUND);

        // 获取对应的岗位
        Set<Long> jobIds = sysUserJobDao.selectList(Wrappers.<SysUserJob>lambdaQuery().eq(SysUserJob::getUserId, id)).stream().map(SysUserJob::getJobId).collect(Collectors.toSet());

        // 获取对应的角色
        Set<Long> roleIds = sysUserRoleDao.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id)).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());

        SysUserDTO userDTO = BeanUtils.cgLibCopyBean(user, SysUserDTO::new);
        userDTO.setJobIds(jobIds);
        userDTO.setRoleIds(roleIds);
        return userDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(SysUserDTO sysUserDTO) {
        SysUser user = BeanUtils.cgLibCopyBean(sysUserDTO, SysUser::new);
        // 校验账号是否存在
        AssertUtils.isNull(lambdaQuery().eq(SysUser::getUsername, sysUserDTO.getUsername()).one(), ResponseCode.ACCOUNT_EXISTS);
        // 保存用户
        save(user);
        // 保存用户与部门,角色的关系
        saveRoleAndJobRelation(sysUserDTO.setId(user.getId()));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(SysUserDTO sysUserDTO) {
        SysUser user = BeanUtils.cgLibCopyBean(sysUserDTO, SysUser::new);
        // 删除用户与部门,角色的关系
        deleteRoleAndJobRelation(Collections.singletonList(sysUserDTO.getId()));
        // 更新角色
        updateById(user);
        // 保存用户与菜单,部门的关系
        saveRoleAndJobRelation(sysUserDTO);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeBatchUser(List<Long> ids) {
        // 删除用户
        removeBatchByIds(ids);
        // 删除角色与菜单,部门的关系
        deleteRoleAndJobRelation(ids);
    }

    /**
     * 保存用户与部门,角色的关系
     *
     * @param sysUserDTO
     */
    private void saveRoleAndJobRelation(SysUserDTO sysUserDTO) {
        //保存用户与岗位关系
        sysUserJobDao.insertBatchUserJob(sysUserDTO.getId(), sysUserDTO.getJobIds());
        //保存户与角色关系
        sysUserRoleDao.insertBatchUserRole(sysUserDTO.getId(), sysUserDTO.getRoleIds());
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
