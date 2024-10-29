package com.ytrue.infra.news.exception;

/**
 * 自定义文件存储异常类。
 * 该异常用于处理与文件存储相关的错误情况。
 *
 * @author ytrue
 * @date 2023/04/25
 */
public class FileStorageException extends RuntimeException {

    /**
     * 默认构造函数。
     * 创建一个新的 FileStorageException 实例。
     */
    public FileStorageException() {
    }

    /**
     * 带有详细消息的构造函数。
     *
     * @param message 异常消息，提供有关错误的描述
     */
    public FileStorageException(String message) {
        super(message);
    }

    /**
     * 带有详细消息和根本原因的构造函数。
     *
     * @param message 异常消息，提供有关错误的描述
     * @param cause   引发该异常的原始原因
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 带有根本原因的构造函数。
     *
     * @param cause 引发该异常的原始原因
     */
    public FileStorageException(Throwable cause) {
        super(cause);
    }

    /**
     * 带有详细消息、根本原因、抑制选项和可写堆栈跟踪的构造函数。
     *
     * @param message            异常消息，提供有关错误的描述
     * @param cause              引发该异常的原始原因
     * @param enableSuppression  是否可以抑制其他异常
     * @param writableStackTrace 是否允许堆栈跟踪可写
     */
    public FileStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
