package com.ytrue.tools.security.excptions;

/**
 * @author ytrue
 * @description: 授权失败异常
 * @date 2022/12/21 8:44
 */
public class AccessDeniedFailureException extends RuntimeException {
    private static final long serialVersionUID = 4036318791868985258L;

    public AccessDeniedFailureException(String message) {
        super(message);
    }

    public AccessDeniedFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedFailureException(Throwable cause) {
        super(cause);
    }
}
