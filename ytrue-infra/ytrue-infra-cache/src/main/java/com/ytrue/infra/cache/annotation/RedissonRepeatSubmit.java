package com.ytrue.infra.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2023-11-28 15:30
 * @description 防重提交 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedissonRepeatSubmit {

    /**
     * key 可为空，已保证唯一性
     */
    String value();

    /**
     * key过期时间
     */
    long expireTime() default 10L;


    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 过期方式
     * <p>
     * true 默认 等待时间自动过期
     * false 方法执行完毕后后过期
     */
    boolean waitExpire() default true;

    /**
     * 是否 校验request表单内容
     */
    boolean validateForm() default true;

    /**
     * 异常message信息
     *
     * @return
     */
    String failMessage() default "频繁提交";

    /**
     * 如果keys有多个, 使用:拼接  value:key1:key2
     *
     * @return
     */
    String[] keys();
}
