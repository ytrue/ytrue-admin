package com.ytrue.bean.resp.system;

import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.dataobject.system.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class SysUserLoginInfoResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "用户信息")
    private SysUser user;

    @Schema(description = "部门信息")
    private SysDeptIdResp dept;

    @Schema(description = "角色信息")
    private List<SysRoleListResp> roles;

    @Schema(description = "岗位信息")
    private List<SysJobListResp> jobs;

    @Schema(description = "角色code")
    private Set<String> roleCodes;

    @Schema(description = "权限标识")
    private Set<String> permissions;
}
