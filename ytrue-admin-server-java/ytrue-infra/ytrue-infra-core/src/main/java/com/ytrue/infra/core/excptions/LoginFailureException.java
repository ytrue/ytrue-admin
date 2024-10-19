package com.ytrue.infra.core.excptions;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;

/**
 * LoginFailureException 类表示登录失败异常，用于处理用户登录过程中的错误情况。
 *
 * <p>
 * 该异常用于封装与用户登录失败相关的错误信息，以便在登录失败时提供明确的反馈。
 * </p>
 *
 * @author ytrue
 * @description 登录失败异常
 * @date 2022/12/21 9:23
 */
public class LoginFailureException extends BaseCodeException {
    @Serial
    private static final long serialVersionUID = -3913654692436161922L;

    /**
     * 默认构造函数。
     */
    public LoginFailureException() {
    }

    /**
     * 使用指定的错误消息创建 LoginFailureException 实例。
     *
     * @param message 错误消息
     */
    public LoginFailureException(String message) {
        super(message);
    }

    /**
     * 使用指定的状态码和错误消息创建 LoginFailureException 实例。
     *
     * @param code 错误状态码
     * @param message 错误消息
     */
    public LoginFailureException(String code, String message) {
        super(code, message);
    }

    /**
     * 使用指定的错误消息和引发该异常的原因创建 LoginFailureException 实例。
     *
     * @param message 错误消息
     * @param cause 引发该异常的原因
     */
    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用服务器响应信息接口的实现创建 LoginFailureException 实例。
     *
     * @param responseCode 响应信息接口的实现，包含状态码和消息
     */
    public LoginFailureException(IServerResponseInfo responseCode) {
        super(responseCode);
    }
}
