package com.ytrue.modules.system.model.vo;

import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.model.po.SysJob;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.po.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserInfoVO
 * @date 2022/12/8 15:53
 */
@Data
public class SysUserInfoVO {
    @Schema(title = "用户信息")
    private SysUser user;

    @Schema(title = "部门信息")
    private SysDept dept;

    @Schema(title = "角色信息")
    private Set<SysRole> roles;

    @Schema(title = "岗位信息")
    private List<SysJob> jobs;

    @Schema(title = "角色code")
    private Set<String> roleCodes;

    @Schema(title = "权限标识")
    private Set<String> permissions;
}
