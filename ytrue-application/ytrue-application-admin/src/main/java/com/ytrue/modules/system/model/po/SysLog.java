package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "操作IP")
    private String requestIp;

    @ApiModelProperty(value = "日志类型 OPT:操作类型;EX:异常类型")
    private String type;

    @ApiModelProperty(value = "操作描述")
    private String description;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "类路径")
    private String classPath;

    @ApiModelProperty(value = "请求类型")
    private String actionMethod;

    @ApiModelProperty(value = "请求地址")
    private String requestUri;

    @ApiModelProperty(value = "请求类型 GET;POST;PUT;DELETE;PATCH;TRACE;HEAD;OPTIONS")
    private String httpMethod;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "返回值")
    private String result;

    @ApiModelProperty(value = "异常详情信息")
    private String exDesc;

    @ApiModelProperty(value = "异常描述")
    private String exDetail;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "消耗时间")
    private Long consumingTime;

    @ApiModelProperty(value = "浏览器")
    private String browser;
}
