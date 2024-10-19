package com.ytrue.infra.redis.excptions;

/**
 * @author ytrue
 * @date 2023-12-04 16:58
 * @description RedissonRateLimiter
 */
public class RedissonRateLimiterFailureException extends RuntimeException {

    public RedissonRateLimiterFailureException() {
    }

    public RedissonRateLimiterFailureException(String message) {
        super(message);
    }

    public RedissonRateLimiterFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
