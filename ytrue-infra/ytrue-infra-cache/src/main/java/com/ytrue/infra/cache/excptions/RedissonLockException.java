package com.ytrue.infra.cache.excptions;

import com.ytrue.infra.core.base.BaseCodeException;

/**
 * @author ytrue
 * @date 2023-11-28 17:28
 * @description RedissonLockException
 */
public class RedissonLockException extends BaseCodeException {

    public RedissonLockException() {
    }

    public RedissonLockException(String message) {
        super(message);
    }

    public RedissonLockException(String message, Throwable cause) {
        super(message, cause);
    }

}
