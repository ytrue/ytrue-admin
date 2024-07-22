package com.ytrue.infra.cache.strategy;

import com.ytrue.infra.cache.annotation.RedissonRateLimiter;
import com.ytrue.infra.cache.excptions.RedissonRateLimiterFailureException;

import java.lang.reflect.Method;

public class DefaultRedissonRateLimiterFailureStrategy implements RedissonRateLimiterFailureStrategy {

    @Override
    public void onFailure(RedissonRateLimiter limiter, Method method, Object[] arguments) {
        throw new RedissonRateLimiterFailureException("访问频繁");
    }
}
