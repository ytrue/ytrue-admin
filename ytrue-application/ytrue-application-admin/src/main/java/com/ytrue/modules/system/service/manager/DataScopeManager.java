package com.ytrue.modules.system.service.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.modules.system.dao.SysDeptDao;
import com.ytrue.modules.system.dao.SysRoleDao;
import com.ytrue.modules.system.dao.SysRoleDeptDao;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.enums.DataScopeEnum;
import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.po.SysRoleDept;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.tools.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @description: 数据范围
 * @date 2022/12/29 9:06
 */
@Component
@AllArgsConstructor
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
        if (CollectionUtil.isNotEmpty(deptIds)) {
            if (!deptIds.contains(0L)) {
                roleIds = sysRoleDeptDao.selectList(Wrappers.<SysRoleDept>lambdaQuery().in(SysRoleDept::getDeptId, deptIds)).stream().map(SysRoleDept::getRoleId).collect(Collectors.toSet());
                // 要把当前账号的角色放入进去，如果级别包含本人
                String userId = SecurityUtils.getLoginUser().getUser().getUserId();

                roleIds.addAll(sysRoleDao.listByUserId(Convert.toLong(userId)).stream().map(SysRole::getId).collect(Collectors.toSet()));
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
        if (sysUser.getIsAdmin()) {
            return Collections.emptySet();
        }
        // 获取角色集合
        List<SysRole> roleList = sysRoleDao.selectList(Wrappers.<SysRole>lambdaQuery().in(SysRole::getRoleCode, roleCodes));

        // 判断是否包含全部数据,包含就不处理了
        SysRole sysRole1 = roleList.stream().filter(sysRole -> sysRole.getDataScope().equals(DataScopeEnum.DATA_SCOPE_ALL.getValue())).findFirst().orElse(null);
        if (null != sysRole1) {
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
        if (CollectionUtil.isNotEmpty(ids)) {
            resultIds.addAll(ids);
            for (Long id : ids) {
                resultIds.addAll(getDeptAndChildIds(id));
            }
        }
        return resultIds;
    }
}
