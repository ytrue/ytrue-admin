package com.ytrue.infra.cache.annotation;

import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.*;

/**
 * @author ytrue
 * @date 2023-11-28 15:30
 * @description 限流器 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedissonRateLimiter {

    /**
     * key 可为空，已保证唯一性
     */
    String value();


    /**
     * 速率 默认100
     */
    long rate() default 100L;

    /**
     * 时间 默认1分钟
     */
    long rateInterval() default 1L;

    /**
     * 时间单位 默认分钟
     */
    RateIntervalUnit timeUnit() default RateIntervalUnit.MINUTES;

    /**
     * 如果keys有多个, 使用:拼接  value:key1:key2
     *
     * @return
     */
    String[] keys() default {};

    /**
     * 异常message信息
     *
     * @return
     */
    String failMessage() default "访问频繁";
}
