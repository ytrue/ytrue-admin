package com.ytrue.infra.cache.strategy;

import com.ytrue.infra.cache.annotation.RedissonLock;

import java.lang.reflect.Method;

public interface RedissonLockFailureStrategy {

    /**
     * 失败处理
     * @param lock
     * @param method
     * @param arguments
     */
    void onFailure(RedissonLock lock, Method method, Object[] arguments);

}
