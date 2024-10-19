package com.ytrue.infra.operationlog.enitiy;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @date 2022/6/1 13:39
 * @description 实体类 系统日志
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog implements Serializable {


    @Serial
    private static final long serialVersionUID = -6604255398447349464L;
    /**
     * 操作IP
     */
    private String requestIp;

    /**
     * 日志类型,这里的异常是系统异常
     * #LogType{OPT:操作类型;EX:异常类型}
     */
    private String type;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 请求类型
     */
    private String actionMethod;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求类型
     * #HttpMethod{GET;POST;PUT;DELETE;PATCH;TRACE;HEAD;OPTIONS;}
     */
    private String httpMethod;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回值
     */
    private String result;

    /**
     * 异常详情信息
     */
    private String exDesc;

    /**
     * 异常描述
     */
    private String exDetail;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 消耗时间
     */
    private Long consumingTime;

    /**
     * 浏览器
     */
    private String browser;

}
