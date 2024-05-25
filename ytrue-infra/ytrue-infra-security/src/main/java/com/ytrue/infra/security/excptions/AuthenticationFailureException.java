package com.ytrue.infra.security.excptions;

/**
 * @author ytrue
 * @description: 认证失败异常
 * @date 2022/12/21 8:45
 */
public class AuthenticationFailureException extends RuntimeException {
    private static final long serialVersionUID = -3914173733369302977L;

    public AuthenticationFailureException(String message) {
        super(message);
    }

    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailureException(Throwable cause) {
        super(cause);

    }
}
