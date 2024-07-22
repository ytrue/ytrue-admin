package com.ytrue.infra.cache.excptions;

/**
 * @author ytrue
 * @date 2023-12-04 16:59
 * @description RedissonRepeatSubmit
 */
public class RedissonRepeatSubmitFailureException extends RuntimeException {

    public RedissonRepeatSubmitFailureException() {
    }

    public RedissonRepeatSubmitFailureException(String message) {
        super(message);
    }

    public RedissonRepeatSubmitFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
