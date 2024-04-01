package com.ytrue.infra.cache.excptions;

import com.ytrue.infra.core.base.BaseCodeException;

/**
 * @author ytrue
 * @date 2023-12-04 16:58
 * @description RedissonRateLimiter
 */
public class RedissonRateLimiterException extends BaseCodeException {

    public RedissonRateLimiterException() {
    }

    public RedissonRateLimiterException(String message) {
        super(message);
    }

    public RedissonRateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }
}
