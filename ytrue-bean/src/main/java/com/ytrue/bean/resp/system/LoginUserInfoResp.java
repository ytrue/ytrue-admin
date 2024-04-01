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

/**
 * @author ytrue
 * @description: LoginUserInfoResp
 * @date 2022/12/8 15:53
 */
@Data
public class LoginUserInfoResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "用户信息")
    private SysUser user;

    @Schema(description = "部门信息")
    private SysDept dept;

    @Schema(description = "角色信息")
    private Set<SysRole> roles;

    @Schema(description = "岗位信息")
    private List<SysJob> jobs;

    @Schema(description = "角色code")
    private Set<String> roleCodes;

    @Schema(description = "权限标识")
    private Set<String> permissions;
}
