package com.ytrue.infra.redis.strategy;

import com.ytrue.infra.redis.annotation.RedissonLock;
import com.ytrue.infra.redis.excptions.RedissonLockFailureException;

import java.lang.reflect.Method;

public class DefaultRedissonLockFailureStrategy implements RedissonLockFailureStrategy {

    @Override
    public void onFailure(RedissonLock lock, Method method, Object[] arguments) {
        throw new RedissonLockFailureException("获锁失败");
    }
}
