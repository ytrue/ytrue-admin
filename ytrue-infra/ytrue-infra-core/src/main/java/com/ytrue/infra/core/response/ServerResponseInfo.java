package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseInfo;

/**
 * @param code    状态码
 * @param message 信息
 */
public record ServerResponseInfo(String code, String message) implements IServerResponseInfo {

    public static IServerResponseInfo error(String message) {
        return new ServerResponseInfo(ResponseInfoEnum.CUSTOM_FAIL_MESSAGE.code(), message);
    }

    public static IServerResponseInfo error(String code, String message) {
        return new ServerResponseInfo(code, message);
    }
}
