package com.ytrue.infra.cache.excptions;

/**
 * @author ytrue
 * @date 2023-11-28 17:28
 * @description RedissonLockFailureException
 */
public class RedissonLockFailureException extends RuntimeException {

    public RedissonLockFailureException() {
    }

    public RedissonLockFailureException(String message) {
        super(message);
    }

    public RedissonLockFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
