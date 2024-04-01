package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseCode;

public class ServerResponseCode implements IServerResponseCode {

    /**
     * 状态码
     */
    private final String code;

    /**
     * 信息
     */
    private final String message;


    public ServerResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static IServerResponseCode error(String message) {
        return new ServerResponseCode("5000", message);
    }

    public static IServerResponseCode error(String code, String message) {
        return new ServerResponseCode(code, message);
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
