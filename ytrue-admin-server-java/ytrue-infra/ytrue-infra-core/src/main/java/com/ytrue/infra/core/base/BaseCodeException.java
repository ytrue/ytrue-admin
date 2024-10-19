package com.ytrue.infra.core.base;

import com.ytrue.infra.core.response.ServerResponseInfoEnum;
import lombok.Getter;

import java.io.Serial;

/**
 * @author ytrue
 * @date 2024/10/19
 * @description  基础异常类，用于定义带有状态码的运行时异常。
 *
 * <p>
 * 该类扩展自 {@link RuntimeException}，可以用于业务逻辑中的自定义异常处理。
 * </p>
 */
@Getter
public abstract class BaseCodeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5873796476107436739L; // 序列化ID

    /**
     * 状态码，默认为内部服务器错误状态码。
     */
    private String code = ServerResponseInfoEnum.INTERNAL_SERVER_ERROR.code();

    /**
     * 默认构造函数。
     */
    public BaseCodeException() {
    }

    /**
     * 带消息的构造函数。
     *
     * @param message 异常消息
     */
    public BaseCodeException(String message) {
        super(message);
    }

    /**
     * 带状态码和消息的构造函数。
     *
     * @param code 状态码
     * @param message 异常消息
     */
    public BaseCodeException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 带消息和原因的构造函数。
     *
     * @param message 异常消息
     * @param cause 导致该异常的根本原因
     */
    public BaseCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用服务器响应信息构造异常。
     *
     * @param responseCode 服务器响应信息
     */
    public BaseCodeException(IServerResponseInfo responseCode) {
        super(responseCode.message());
        this.code = responseCode.code();
    }

}
