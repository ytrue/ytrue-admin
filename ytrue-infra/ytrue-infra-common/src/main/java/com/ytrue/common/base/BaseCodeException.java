package com.ytrue.common.base;

import lombok.Getter;

/**
 * @author ytrue
 * @date 2022/4/20 17:40
 * @description 基础异常, 这个在全局异常要获得message和code的
 */
@Getter
public abstract class BaseCodeException extends RuntimeException {

    private static final long serialVersionUID = 5873796476107436739L;
    /**
     * 状态码
     */
    private Integer code;

    public BaseCodeException() {
    }

    public BaseCodeException(String message) {
        super(message);
    }

    public BaseCodeException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseCodeException(IBaseExceptionCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

}
