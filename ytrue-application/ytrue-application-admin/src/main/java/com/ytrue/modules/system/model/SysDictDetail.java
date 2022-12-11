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
 * @description: SysDictDetail
 * @date 2022/12/7 9:05
 */
@Data
@TableName("sys_dict_detail")
@Accessors(chain = true)
public class SysDictDetail implements Serializable {
    private static final long serialVersionUID = 2075740730368465063L;

    @TableId
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "字典Id", hidden = true)
    private Long dictId;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer dictSort;

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
