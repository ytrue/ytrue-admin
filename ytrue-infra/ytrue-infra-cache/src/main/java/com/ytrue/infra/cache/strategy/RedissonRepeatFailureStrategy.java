package com.ytrue.infra.cache.strategy;

import com.ytrue.infra.cache.annotation.RedissonRepeatSubmit;

import java.lang.reflect.Method;

public interface RedissonRepeatFailureStrategy {

    /**
     * 失败处理
     *
     * @param repeatSubmit
     * @param method
     * @param arguments
     */
    void onFailure(RedissonRepeatSubmit repeatSubmit, Method method, Object[] arguments);
}
