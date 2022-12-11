package com.ytrue.modules.system.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysMenu
 * @date 2022/12/7 14:09
 */
@Data
@TableName("sys_menu")
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = -2375558178587426862L;

    @TableId
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID")
    private Long pid;

    @ApiModelProperty(value = "显示顺序")
    private Integer menuSort;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "路由参数")
    private String query;

    @ApiModelProperty(value = " 是否为外链（0是 1否）")
    private Boolean isFrame;

    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    private Boolean isCache;

    @ApiModelProperty(value = "类型（M目录 C菜单 F按钮）")
    private String menuType;

    @ApiModelProperty(value = "显示状态（0显示 1隐藏）")
    private Boolean visible;

    @ApiModelProperty(value = " 菜单状态（0正常 1停用）")
    private Boolean status;

    @ApiModelProperty(value = "权限字符串")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "子节点数目", hidden = true)
    private Integer subCount;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
