package com.ytrue.infra.cache.strategy;

import com.ytrue.infra.cache.annotation.RedissonLock;
import com.ytrue.infra.cache.excptions.RedissonLockFailureException;

import java.lang.reflect.Method;

public class DefaultRedissonLockFailureStrategy implements RedissonLockFailureStrategy {

    @Override
    public void onFailure(RedissonLock lock, Method method, Object[] arguments) {
        throw new RedissonLockFailureException("获锁失败");
    }
}
