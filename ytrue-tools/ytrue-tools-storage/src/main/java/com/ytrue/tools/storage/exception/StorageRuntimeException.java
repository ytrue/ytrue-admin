package com.ytrue.tools.storage.exception;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description StorageRuntimeException
 */
public class StorageRuntimeException extends RuntimeException {
    public StorageRuntimeException() {
    }

    public StorageRuntimeException(String message) {
        super(message);
    }

    public StorageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageRuntimeException(Throwable cause) {
        super(cause);
    }

    public StorageRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
