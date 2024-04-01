package com.ytrue.infra.cache.annotation;

import com.ytrue.infra.cache.enums.RedissonLockTypeEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2023-11-28 15:29
 * @description 分布式锁
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedissonLock {
    /**
     * 锁的模式:如果不设置,自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE
     */
    RedissonLockTypeEnum type();

    /**
     * 如果keys有多个, 使用:拼接  value:key1:key2
     *
     * @return
     */
    String[] keys() default {};


    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 分布式锁key，可为空，已保证唯一性
     *
     * @return
     */
    String value();

    /**
     * 等待超时时间，默认30
     *
     * @return
     */
    long waitTime() default 30L;


    /**
     * 自动解锁时间，必须大于方法执行时间，默认60
     */
    long leaseTime() default 60L;

    /**
     * 异常message信息
     *
     * @return
     */
    String failMessage() default "获锁失败";
}
