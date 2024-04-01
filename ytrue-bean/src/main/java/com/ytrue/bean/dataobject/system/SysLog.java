package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ytrue.bean.dataobject.BaseIdEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysLog
 * @date 2022/12/9 9:58
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_log")
public class SysLog extends BaseIdEntity {

    @Serial
    private static final long serialVersionUID = 7415492376407370163L;


    @Schema(description = "操作IP")
    private String requestIp;

    @Schema(description = "日志类型 OPT:操作类型;EX:异常类型")
    private String type;

    @Schema(description = "操作描述")
    private String description;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "类路径")
    private String classPath;

    @Schema(description = "请求类型")
    private String actionMethod;

    @Schema(description = "请求地址")
    private String requestUri;

    @Schema(description = "请求类型 GET;POST;PUT;DELETE;PATCH;TRACE;HEAD;OPTIONS")
    private String httpMethod;

    @Schema(description = "请求参数")
    private String params;

    @Schema(description = "返回值")
    private String result;

    @Schema(description = "异常详情信息")
    private String exDesc;

    @Schema(description = "异常描述")
    private String exDetail;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "消耗时间")
    private Long consumingTime;

    @Schema(description = "浏览器")
    private String browser;


  
}
