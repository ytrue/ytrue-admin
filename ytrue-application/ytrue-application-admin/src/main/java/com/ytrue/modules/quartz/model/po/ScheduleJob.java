package com.ytrue.modules.quartz.model.po;

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
import java.util.Date;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description ScheduleJob
 */
@Data
@TableName("schedule_job")

public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 9150018026309004044L;

    @TableId
    @ApiModelProperty(value = "ID")
    private Long id;

    @NotBlank(message = "bean名称不能为空")
    @ApiModelProperty(value = "bean名称")
    private String beanName;

    @NotBlank(message = "方法名称不能为空")
    @ApiModelProperty(value = "方法名")
    private String methodName;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "cron表达式不能为空")
    private String cronExpression;

    @ApiModelProperty(value = "任务状态:0=正常,1=暂停")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

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
