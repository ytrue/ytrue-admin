package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ytrue.common.constant.TimeFormat;
import com.ytrue.common.constant.TimeZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysRole
 * @date 2022/12/7 15:05
 */
@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = 383926519508416659L;

    @TableId
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

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;
}
