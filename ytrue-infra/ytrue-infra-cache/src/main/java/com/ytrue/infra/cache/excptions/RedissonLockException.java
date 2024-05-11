package com.ytrue.infra.cache.excptions;

/**
 * @author ytrue
 * @date 2023-11-28 17:28
 * @description RedissonLockException
 */
public class RedissonLockException extends RuntimeException {

    public RedissonLockException() {
    }

    public RedissonLockException(String message) {
        super(message);
    }

    public RedissonLockException(String message, Throwable cause) {
        super(message, cause);
    }

}
