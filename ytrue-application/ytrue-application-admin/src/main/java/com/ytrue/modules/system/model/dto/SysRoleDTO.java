package com.ytrue.modules.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @ApiModelProperty(value = "菜单id集合")
    private Set<Long> menuIds;

    @ApiModelProperty(value = "角色标识")
    private String code;

    @ApiModelProperty(value = "部门id集合")
    private Set<Long> deptIds;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "数据权限，全部 、 本级 、 自定义")
    private String dataScope;

    @ApiModelProperty(value = "排序")
    private Integer roleSort;

    @ApiModelProperty(value = "描述")
    private String description;
}
