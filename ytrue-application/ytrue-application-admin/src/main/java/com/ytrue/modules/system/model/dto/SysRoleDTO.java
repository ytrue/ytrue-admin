package com.ytrue.modules.system.model.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleDTO
 * @date 2022/12/7 15:29
 */
@Data

public class SysRoleDTO implements Serializable {

    private static final long serialVersionUID = -2368863016297932647L;


    @ApiModelProperty(value = "id")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @NotBlank
    @ApiModelProperty(value = "角色标识")
    private String roleCode;

    @ApiModelProperty(value = "数据范围:1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer dataScope;

    @ApiModelProperty(value = "排序")
    private Integer roleSort;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态:0=禁用,1=正常")
    private Boolean status;

    @ApiModelProperty(value = "菜单树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean menuCheckStrictly;

    @ApiModelProperty(value = "部门树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean deptCheckStrictly;

    @ApiModelProperty(value = "菜单id集合")
    private Set<Long> menuIds;

    @ApiModelProperty(value = "部门id集合")
    private Set<Long> deptIds;
}
