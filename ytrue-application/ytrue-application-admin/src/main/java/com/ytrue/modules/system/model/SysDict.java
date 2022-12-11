package com.ytrue.modules.system.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: Dict
 * @date 2022/12/7 9:05
 */
@Data
@TableName("sys_dict")
@Accessors(chain = true)
public class SysDict implements Serializable {

    private static final long serialVersionUID = -7834091885145301189L;

    @TableId
    @ApiModelProperty(value = "id")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

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
