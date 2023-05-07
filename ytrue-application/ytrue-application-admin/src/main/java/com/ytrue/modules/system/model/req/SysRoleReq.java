package com.ytrue.modules.system.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleReq
 * @date 2022/12/7 15:29
 */
@Data
public class SysRoleReq implements Serializable {

    private static final long serialVersionUID = -2368863016297932647L;


    @Schema(title = "id")
    private Long id;

    @NotBlank
    @Schema(title = "角色名称")
    private String roleName;

    @NotBlank
    @Schema(title = "角色标识")
    private String roleCode;

    @Schema(title = "数据范围:1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer dataScope;

    @Schema(title = "排序")
    private Integer roleSort;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "状态:0=禁用,1=正常")
    private Boolean status;

    @Schema(title = "菜单树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean menuCheckStrictly;

    @Schema(title = "部门树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean deptCheckStrictly;

    @Schema(title = "菜单id集合")
    private Set<Long> menuIds;

    @Schema(title = "部门id集合")
    private Set<Long> deptIds;
}
