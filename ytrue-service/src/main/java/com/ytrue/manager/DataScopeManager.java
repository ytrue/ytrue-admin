package com.ytrue.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.dataobject.system.SysRoleDept;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.enums.system.DataScopeEnum;
import com.ytrue.infra.db.dao.system.SysDeptDao;
import com.ytrue.infra.db.dao.system.SysRoleDao;
import com.ytrue.infra.db.dao.system.SysRoleDeptDao;
import com.ytrue.infra.db.dao.system.SysUserDao;
import com.ytrue.tools.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @description: 数据范围
 * @date 2022/12/29 9:06
 */
@Component
@RequiredArgsConstructor
public class DataScopeManager {

    private final SysUserDao sysUserDao;
    private final SysRoleDao sysRoleDao;
    private final SysRoleDeptDao sysRoleDeptDao;
    private final SysDeptDao sysDeptDao;

    /**
     * 获取当前账号能得到的角色id
     *
     * @return
     */
    public Set<Long> listRoleIdDataScope() {
        // 处理下数据过滤
        Set<Long> deptIds = this.listDeptIdDataScope();
        Set<Long> roleIds = new HashSet<>();
        // 如果包含0呢
        if (CollUtil.isNotEmpty(deptIds)) {
            if (!deptIds.contains(0L)) {
                roleIds = sysRoleDeptDao.selectList(Wrappers.<SysRoleDept>lambdaQuery().in(SysRoleDept::getDeptId, deptIds)).stream().map(SysRoleDept::getRoleId).collect(Collectors.toSet());
                // 要把当前账号的角色放入进去，如果级别包含本人
                String userId = SecurityUtils.getLoginUser().getUser().getUserId();
                roleIds.addAll(sysRoleDao.selectBySysUserId(Long.valueOf(userId)).stream().map(SysRole::getId).collect(Collectors.toSet()));
            } else {
                roleIds = deptIds;
            }
        }
        return roleIds;
    }

    /**
     * 获取当前账号能得到的部门id
     *
     * @return
     */
    public Set<Long> listDeptIdDataScope() {
        // 返回的数据
        Set<Long> resultIds = new HashSet<>();

        // 获取用户角色
        Set<String> roleCodes = SecurityUtils.getLoginUser().getUser().getRoles();
        SysUser sysUser = sysUserDao.selectById(SecurityUtils.getLoginUser().getUser().getUserId());

        // 超级管理员不做限制
        if (sysUser.getAdmin()) {
            return Collections.emptySet();
        }
        // 获取角色集合
        List<SysRole> roleList = sysRoleDao.selectList(Wrappers.<SysRole>lambdaQuery().in(SysRole::getRoleCode, roleCodes));

        // 判断是否包含全部数据,包含就不处理了
        SysRole sysRole1 = roleList.stream().filter(sysRole -> sysRole.getDataScope().equals(DataScopeEnum.DATA_SCOPE_ALL.getValue())).findFirst().orElse(null);
        if (Objects.nonNull(sysRole1)) {
            return Collections.emptySet();
        }

        // 循环处理
        for (SysRole sysRole : roleList) {
            // 获取角色的数据范围
            Integer dataScope = sysRole.getDataScope();

            // 自定义数据范围
            if (dataScope.equals(DataScopeEnum.DATA_SCOPE_CUSTOM.getValue())) {
                Set<Long> customDeptIds = sysRoleDeptDao
                        .selectList(Wrappers.<SysRoleDept>lambdaQuery().eq(SysRoleDept::getRoleId, sysRole.getId()))
                        .stream().map(SysRoleDept::getDeptId)
                        .collect(Collectors.toSet());

                // 追加
                resultIds.addAll(customDeptIds);
            }

            // 部门数据范围
            if (dataScope.equals(DataScopeEnum.DATA_SCOPE_DEPT.getValue())) {
                resultIds.add(sysUser.getDeptId());
            }

            // 部门及以下数据权限
            if (dataScope.equals(DataScopeEnum.DATA_SCOPE_DEPT_AND_CHILD.getValue())) {
                // 这个要递归处理
                resultIds.addAll(getDeptAndChildIds(sysUser.getDeptId()));
            }

            // 仅本人数据权限
            if (dataScope.equals(DataScopeEnum.DATA_SCOPE_SELF.getValue())) {
                // 仅本人数据权限,是查询不到任何人数据的，用户id就是本人，用户列表只有自己，部门呀什么的没有
                resultIds.add(0L);
            }
        }

        // 如果元素大于1 ,就把0去掉
        if (resultIds.size() > 1) {
            resultIds.remove(0L);
        }

        return resultIds;
    }


    /**
     * 部门及以下id
     *
     * @param deptId
     * @return
     */
    private Set<Long> getDeptAndChildIds(Long deptId) {
        HashSet<Long> resultIds = new HashSet<>();
        resultIds.add(deptId);
        Set<Long> ids = sysDeptDao.selectList(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getPid, deptId)).stream().map(SysDept::getId).collect(Collectors.toSet());

        // 为空就返回了
        if (CollUtil.isEmpty(ids)) {
            return resultIds;
        }


        resultIds.addAll(ids);
        for (Long id : ids) {
            resultIds.addAll(getDeptAndChildIds(id));
        }
        return resultIds;
    }
}
