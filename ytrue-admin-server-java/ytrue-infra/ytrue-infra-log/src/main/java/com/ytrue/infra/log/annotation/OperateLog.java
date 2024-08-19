package com.ytrue.infra.log.annotation;

import java.lang.annotation.*;

/**
 * @author ytrue
 * @date 2022/6/1 13:39
 * @description 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 描述
     *
     * @return {String}
     */
    String value() default "";
}
