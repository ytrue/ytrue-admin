package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ytrue.common.constant.DataFormat;
import com.ytrue.common.constant.TimeZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysMenu
 * @date 2022/12/7 14:09
 */
@Data
public class SysMenu implements Serializable {

    private static final long serialVersionUID = -2375558178587426862L;

    @TableId
    @Schema(title = "id")
    private Long id;

    @Schema(title = "菜单名称")
    private String menuName;

    @Schema(title = "父菜单ID")
    private Long pid;

    @Schema(title = "显示顺序")
    private Integer menuSort;

    @Schema(title = "路由地址")
    private String path;

    @Schema(title = "组件路径")
    private String component;

    @Schema(title = "路由参数")
    private String query;

    @Schema(title = " 是否为外链（0是 1否）")
    private Boolean isFrame;

    @Schema(title = "是否缓存（0缓存 1不缓存）")
    private Boolean isCache;

    @Schema(title = "类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(title = "显示状态（0显示 1隐藏）")
    private Boolean visible;

    @Schema(title = " 菜单状态（0正常 1停用）")
    private Boolean status;

    @Schema(title = "权限字符串")
    private String perms;

    @Schema(title = "菜单图标")
    private String icon;

    @Schema(title = "子节点数目", hidden = true)
    private Integer subCount;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = DataFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DataFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = DataFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DataFormat.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;
}
