package com.ytrue.infra.operationlog.aspect;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.google.gson.Gson;
import com.ytrue.infra.operationlog.enitiy.OperationLog;
import com.ytrue.infra.operationlog.event.OperateLogEvent;
import com.ytrue.infra.operationlog.util.IpUtil;
import com.ytrue.infra.operationlog.util.OperateLogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2021/7/1 13:39
 * @description 操作日志使用spring event异步入库
 */
@Slf4j
@Aspect
public class OperateLogAspect {

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private HttpServletRequest request;

    private static final ThreadLocal<OperationLog> OPERATION_LOG_THREAD_LOCAL = ThreadLocal.withInitial(OperationLog::new);

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 线程池
     */
    private static final ThreadPoolExecutor SYS_LOG_EVENT_PUSH_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            0,
            Runtime.getRuntime().availableProcessors(),
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(2000),
            ThreadFactoryBuilder.create().setNamePrefix("operate_log_event_push_thread-").build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );


    /***
     * 定义controller切入点拦截规则，拦截SysLog注解的方法
     */
    @Pointcut("@annotation(com.ytrue.infra.operationlog.annotation.OperateLog)")
    public void operateLogAspect() {

    }


    /**
     * 前置
     *
     * @param joinPoint
     */
    @Before(value = "operateLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        // 获取OperationLog
        OperationLog operationLog = OPERATION_LOG_THREAD_LOCAL.get();

        String controllerDescription = "";
        //获取目标类上的 @Api 注解
        Tag tag = joinPoint.getTarget().getClass().getAnnotation(Tag.class);

        if (tag != null) {
            String name = tag.name();
            if (StrUtil.isNotEmpty(name)) {
                controllerDescription = name;
            }
        }

        //获取标注在方法上的 OperateLog 注解的描述
        String controllerMethodDescription = OperateLogUtil.getControllerMethodDescription(joinPoint);

        //判断这个sysLog注解的value是否为空,等于空就去获取ApiOperation注解的value
        if (StrUtil.isEmpty(controllerMethodDescription)) {
            //获得切面方法上的指定注解
            Class<?> clazz = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            Method method;
            try {
                method = clazz.getMethod(methodName, parameterTypes);
                controllerMethodDescription = method.getAnnotation(Operation.class).summary();
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
        operationLog.setParams(StrUtil.sub(strArgs, 0, 65535));
        operationLog.setRequestIp(IpUtil.getClientIP(request));
        operationLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        operationLog.setHttpMethod(request.getMethod());
        operationLog.setBrowser(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
        operationLog.setStartTime(LocalDateTime.now());
        // 设置进去
        OPERATION_LOG_THREAD_LOCAL.set(operationLog);
    }


    /**
     * 返回通知
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "operateLogAspect()")
    public void doAfterReturning(Object ret) {
        try {
            OperationLog operationLog = OPERATION_LOG_THREAD_LOCAL.get();
            operationLog.setResult(new Gson().toJson(ret));
            this.publishOperationLogEvent(operationLog);
        } finally {
            OPERATION_LOG_THREAD_LOCAL.remove();
        }
    }


    /**
     * 异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "operateLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        try {
            OperationLog operationLog = OPERATION_LOG_THREAD_LOCAL.get();
            operationLog.setType("EX");
            // 异常对象
            operationLog.setExDetail(OperateLogUtil.getStackTrace(e));
            // 异常信息
            operationLog.setExDesc(e.getMessage());
            this.publishOperationLogEvent(operationLog);
        } finally {
            OPERATION_LOG_THREAD_LOCAL.remove();
        }
    }


    /**
     * 发送事件
     *
     * @param operationLog
     */
    private void publishOperationLogEvent(OperationLog operationLog) {
        SYS_LOG_EVENT_PUSH_THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                operationLog.setEndTime(LocalDateTime.now());
                operationLog.setConsumingTime(operationLog.getStartTime().until(operationLog.getEndTime(), ChronoUnit.MILLIS));
                applicationContext.publishEvent(new OperateLogEvent(operationLog));
            } catch (Exception exception) {
                log.error("OperateLogAspect publishEvent error", exception);
            }
        });
    }
}
