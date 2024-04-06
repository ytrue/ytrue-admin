package com.ytrue.infra.core.base;

import com.ytrue.infra.core.response.ResponseCodeEnum;
import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class BaseCodeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5873796476107436739L;
    /**
     * 状态码
     */
    private String code = ResponseCodeEnum.FAIL.code();

    public BaseCodeException() {
    }

    public BaseCodeException(String message) {
        super(message);
    }

    public BaseCodeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseCodeException(IServerResponseCode responseCode) {
        super(responseCode.message());
        this.code = responseCode.code();
    }

}