package com.ytrue.tools.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.gson.Gson;
import com.ytrue.tools.log.enitiy.OperationLog;
import com.ytrue.tools.log.event.SysLogEvent;
import com.ytrue.tools.log.utils.SysLogUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2021/7/1 13:39
 * @description 操作日志使用spring event异步入库
 */
@Slf4j
@Aspect
public class SysLogAspect {

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private HttpServletRequest request;

    private static final ThreadLocal<OperationLog> THREAD_LOCAL = new ThreadLocal<>();

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /***
     * 定义controller切入点拦截规则，拦截SysLog注解的方法
     */
    @Pointcut("@annotation(com.ytrue.tools.log.annotation.SysLog)")
    public void sysLogAspect() {

    }

    private OperationLog get() {
        OperationLog sysLog = THREAD_LOCAL.get();
        if (sysLog == null) {
            return new OperationLog();
        }
        return sysLog;
    }


    /**
     * 前置
     *
     * @param joinPoint
     */
    @Before(value = "sysLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        tryCatch((val) -> {
            // 获取OperationLog
            OperationLog operationLog = get();

            String controllerDescription = "";
            //获取目标类上的 @Api 注解
            Api api = joinPoint.getTarget().getClass().getAnnotation(Api.class);

            if (api != null) {
                String[] tags = api.tags();
                if (tags.length > 0) {
                    controllerDescription = tags[0];
                }
            }

            //获取标注在方法上的 SysLog 注解的描述
            String controllerMethodDescription = SysLogUtils.getControllerMethodDescription(joinPoint);

            //判断这个sysLog注解的value是否为空,等于空就去获取ApiOperation注解的value
            if (StrUtil.isEmpty(controllerMethodDescription)) {
                //获得切面方法上的指定注解
                Class<?> clazz = joinPoint.getTarget().getClass();
                String methodName = joinPoint.getSignature().getName();
                Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
                Method method;
                try {
                    method = clazz.getMethod(methodName, parameterTypes);
                    controllerMethodDescription = method.getAnnotation(ApiOperation.class).value();
                } catch (NoSuchMethodException e) {
                    controllerMethodDescription = "";
                }
            }


            //设置操作描述
            if (StrUtil.isEmpty(controllerDescription)) {
                operationLog.setDescription(controllerMethodDescription);
            } else {
                operationLog.setDescription(controllerDescription + "-" + controllerMethodDescription);
            }

            // 设置类名
            operationLog.setClassPath(joinPoint.getTarget().getClass().getName());
            // 设置获取执行的方法名
            operationLog.setActionMethod(joinPoint.getSignature().getName());

            // 参数
            Object[] args = joinPoint.getArgs();

            String strArgs = "";

            try {
                if (!this.request.getContentType().contains(MULTIPART_FORM_DATA)) {
                    Gson gson = new Gson();
                    strArgs = gson.toJson(args);
                }
            } catch (Exception e) {
                try {
                    strArgs = Arrays.toString(args);
                } catch (Exception ex) {
                    log.warn("解析参数异常", ex);
                }
            }
            operationLog.setType("OPT");
            operationLog.setParams(getText(strArgs));
            operationLog.setRequestIp(ServletUtil.getClientIP(request));
            operationLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
            operationLog.setHttpMethod(request.getMethod());
            operationLog.setBrowser(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
            operationLog.setStartTime(LocalDateTime.now());
            THREAD_LOCAL.set(operationLog);
        });
    }


    /**
     * 返回通知
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        tryCatch((aaa) -> {
            OperationLog operationLog = get();
            operationLog.setResult(new Gson().toJson(ret));
            publishEvent(operationLog);
        });

    }


    /**
     * 异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        tryCatch((aaa) -> {
            OperationLog operationLog = get();
            operationLog.setType("EX");
            // 异常对象
            operationLog.setExDetail(SysLogUtils.getStackTrace(e));
            // 异常信息
            operationLog.setExDesc(e.getMessage());
            publishEvent(operationLog);
        });
    }

    /**
     * 截取指定长度的字符串
     *
     * @param val
     * @return
     */
    private String getText(String val) {
        return StrUtil.sub(val, 0, 65535);
    }

    /**
     * 发送事件
     *
     * @param operationLog
     */
    private void publishEvent(OperationLog operationLog) {
        operationLog.setEndTime(LocalDateTime.now());
        operationLog.setConsumingTime(operationLog.getStartTime().until(operationLog.getEndTime(), ChronoUnit.MILLIS));
        applicationContext.publishEvent(new SysLogEvent(operationLog));
        THREAD_LOCAL.remove();
    }


    private void tryCatch(Consumer<String> consumer) {
        try {
            consumer.accept("");
        } catch (Exception e) {
            log.warn("记录操作日志异常", e);
            THREAD_LOCAL.remove();
        }
    }


}
