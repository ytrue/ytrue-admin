package com.ytrue.modules.system.model.po;

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

/**
 * @author ytrue
 * @description: SysLog
 * @date 2022/12/9 9:58
 */
@Data
@TableName("sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 7415492376407370163L;

    @TableId
    @Schema(title = "id")
    private Long id;

    @Schema(title = "操作IP")
    private String requestIp;

    @Schema(title = "日志类型 OPT:操作类型;EX:异常类型")
    private String type;

    @Schema(title = "操作描述")
    private String description;

    @Schema(title = "操作人")
    private String operator;

    @Schema(title = "类路径")
    private String classPath;

    @Schema(title = "请求类型")
    private String actionMethod;

    @Schema(title = "请求地址")
    private String requestUri;

    @Schema(title = "请求类型 GET;POST;PUT;DELETE;PATCH;TRACE;HEAD;OPTIONS")
    private String httpMethod;

    @Schema(title = "请求参数")
    private String params;

    @Schema(title = "返回值")
    private String result;

    @Schema(title = "异常详情信息")
    private String exDesc;

    @Schema(title = "异常描述")
    private String exDetail;

    @Schema(title = "开始时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    @Schema(title = "结束时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime endTime;

    @Schema(title = "消耗时间")
    private Long consumingTime;

    @Schema(title = "浏览器")
    private String browser;
}
