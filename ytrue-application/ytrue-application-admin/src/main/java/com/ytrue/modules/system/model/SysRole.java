package com.ytrue.modules.system.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(value = "id")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "角色标识")
    private String code;

    @ApiModelProperty(value = "数据权限，全部 、 本级 、 自定义")
    private String dataScope;

    @ApiModelProperty(value = "排序")
    private Integer roleSort;

    @ApiModelProperty(value = "描述")
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
