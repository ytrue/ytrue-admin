package com.ytrue.infra.cache.excptions;

/**
 * @author ytrue
 * @date 2023-12-04 16:58
 * @description RedissonRateLimiter
 */
public class RedissonRateLimiterException extends RuntimeException {

    public RedissonRateLimiterException() {
    }

    public RedissonRateLimiterException(String message) {
        super(message);
    }

    public RedissonRateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }
}
