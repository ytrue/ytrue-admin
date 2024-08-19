package com.ytrue.infra.cache.strategy;

import com.ytrue.infra.cache.annotation.RedissonRateLimiter;

import java.lang.reflect.Method;

public interface RedissonRateLimiterFailureStrategy {

    /**
     * 失败处理
     * @param limiter
     * @param method
     * @param arguments
     */
    void onFailure(RedissonRateLimiter limiter, Method method, Object[] arguments);
}
