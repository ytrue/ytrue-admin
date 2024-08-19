package com.ytrue.infra.security.excptions;

/**
 * @author ytrue
 * @description: 无效token异常
 * @date 2022/12/21 9:17
 */
public class InvalidTokenException extends RuntimeException {
    private static final long serialVersionUID = 3136827671299981548L;

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }
}
