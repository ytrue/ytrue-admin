package com.ytrue.common.excptions;

import com.ytrue.common.base.BaseCodeException;
import com.ytrue.common.base.IBaseExceptionCode;

/**
 * @author ytrue
 * @description: 登录失败异常
 * @date 2022/12/21 9:23
 */
public class LoginFailureException extends BaseCodeException {
    private static final long serialVersionUID = -3913654692436161922L;

    public LoginFailureException() {
    }

    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException(Integer code, String message) {
        super(code, message);
    }

    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailureException(IBaseExceptionCode responseCode) {
        super(responseCode);
    }
}
