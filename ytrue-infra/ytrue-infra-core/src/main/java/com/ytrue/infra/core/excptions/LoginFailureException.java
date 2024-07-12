package com.ytrue.infra.core.excptions;


import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;


/**
 * @author ytrue
 * @description: 登录失败异常
 * @date 2022/12/21 9:23
 */
public class LoginFailureException extends BaseCodeException {
    @Serial
    private static final long serialVersionUID = -3913654692436161922L;

    public LoginFailureException() {
    }

    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException(String code, String message) {
        super(code, message);
    }

    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailureException(IServerResponseInfo responseCode) {
        super(responseCode);
    }
}
