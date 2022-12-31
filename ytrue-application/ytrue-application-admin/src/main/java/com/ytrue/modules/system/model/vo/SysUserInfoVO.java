package com.ytrue.modules.system.model.vo;

import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.model.po.SysJob;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.po.SysUser;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "用户信息")
    private SysUser user;

    @ApiModelProperty(value = "部门信息")
    private SysDept dept;

    @ApiModelProperty(value = "角色信息")
    private Set<SysRole> roles;

    @ApiModelProperty(value = "岗位信息")
    private List<SysJob> jobs;

    @ApiModelProperty(value = "角色code")
    private Set<String> roleCodes;

    @ApiModelProperty(value = "权限标识")
    private Set<String> permissions;
}
