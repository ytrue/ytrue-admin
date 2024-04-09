package com.ytrue.bean.resp.system;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ytrue.infra.core.ser.LongCollToStrSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ytrue
 * @date 2023-08-18 11:57
 * @description SysRoleIdResp
 */
@Data
public class SysRoleIdResp implements Serializable {

    @Serial
    private static final long serialVersionUID = -2368863016297932647L;

    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank
    @Schema(description = "角色标识")
    private String roleCode;

    @Schema(description = "数据范围:1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer dataScope;

    @Schema(description = "排序")
    private Integer roleSort;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态:0=禁用,1=正常")
    private Boolean status;

    @Schema(description = "菜单树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean deptCheckStrictly;

    @Schema(description = "菜单id集合")
    @JsonSerialize(using = LongCollToStrSerializer.class)
    private Set<Long> menuIds;


    @Schema(description = "部门id集合")
    @JsonSerialize(using = LongCollToStrSerializer.class)
    private Set<Long> deptIds;
}
