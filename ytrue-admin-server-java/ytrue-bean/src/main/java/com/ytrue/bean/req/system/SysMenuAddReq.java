package com.ytrue.bean.req.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysMenuAddReq {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long pid;

    @Schema(description = "显示顺序")
    private Integer menuSort;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = " 是否为外链（0是 1否）")
    private Boolean isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Boolean isCache;

    @Schema(description = "类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "显示状态（0显示 1隐藏）")
    private Boolean visible;

    @Schema(description = " 菜单状态（0正常 1停用）")
    private Boolean status;

    @Schema(description = "权限字符串")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;
}
