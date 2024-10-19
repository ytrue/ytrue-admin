package com.ytrue.infra.redis.strategy;

import com.ytrue.infra.redis.annotation.RedissonRepeatSubmit;
import com.ytrue.infra.redis.excptions.RedissonLockFailureException;

import java.lang.reflect.Method;

public class DefaultRedissonRepeatSubmitFailureStrategy implements RedissonRepeatFailureStrategy {
    @Override
    public void onFailure(RedissonRepeatSubmit repeatSubmit, Method method, Object[] arguments) {
        throw new RedissonLockFailureException("频繁提交");
    }
}
