package com.ytrue.modules.quartz.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ytrue.common.constant.TimeFormat;
import com.ytrue.common.constant.TimeZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description ScheduleJobLog
 */
@Data
@TableName("schedule_job_log")

public class ScheduleJobLog implements Serializable {
    private static final long serialVersionUID = -4376161257704055582L;
    /**
     * 任务日志id
     */
    @Schema(title = "ID")
    @TableId
    private Long id;

    @Schema(title = "任务id")
    private Long jobId;

    @Schema(title = " spring bean名称")
    private String beanName;

    @Schema(title = "方法名")
    private String methodName;

    @Schema(title = "参数")
    private String params;

    @Schema(title = "任务状态:0=成功,1=失败")
    private Integer status;

    @Schema(title = "失败信息")
    private String error;

    @Schema(title = "耗时(单位：毫秒)")
    private Integer times;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    @JsonFormat(timezone = TimeZone.GMT8, pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;
}
