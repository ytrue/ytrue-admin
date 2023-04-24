package com.ytrue.modules.quartz.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    @Schema(title = "ID")
    private Long id;

    @NotBlank(message = "bean名称不能为空")
    @Schema(title = "bean名称")
    private String beanName;

    @NotBlank(message = "方法名称不能为空")
    @Schema(title = "方法名")
    private String methodName;

    @Schema(title = "参数")
    private String params;

    @Schema(title = "cron表达式不能为空")
    private String cronExpression;

    @Schema(title = "任务状态:0=正常,1=暂停")
    private Integer status;

    @Schema(title = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
