package com.ytrue.infra.news.exception;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description FileStorageException
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException() {
    }

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStorageException(Throwable cause) {
        super(cause);
    }

    public FileStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
