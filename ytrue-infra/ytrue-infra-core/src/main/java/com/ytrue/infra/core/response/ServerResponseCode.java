package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseCode;

/**
 * @param code    状态码
 * @param message 信息
 */
public record ServerResponseCode(String code, String message) implements IServerResponseCode {

    public static IServerResponseCode error(String message) {
        return new ServerResponseCode("5000", message);
    }

    public static IServerResponseCode error(String code, String message) {
        return new ServerResponseCode(code, message);
    }
}
