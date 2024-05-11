package com.ytrue.infra.cache.excptions;

/**
 * @author ytrue
 * @date 2023-12-04 16:59
 * @description RedissonRepeatSubmit
 */
public class RedissonRepeatSubmitException extends RuntimeException {

    public RedissonRepeatSubmitException() {
    }

    public RedissonRepeatSubmitException(String message) {
        super(message);
    }

    public RedissonRepeatSubmitException(String message, Throwable cause) {
        super(message, cause);
    }
}
