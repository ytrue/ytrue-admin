package com.ytrue.infra.redis.strategy;

import com.ytrue.infra.redis.annotation.RedissonLock;

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
