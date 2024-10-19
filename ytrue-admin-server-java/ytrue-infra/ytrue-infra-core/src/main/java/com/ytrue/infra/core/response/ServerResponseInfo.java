package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseInfo;

/**
 * ServerResponseInfo 类用于封装服务器响应的状态码和消息信息。
 *
 * @param code    状态码
 * @param message 信息
 */
public record ServerResponseInfo(String code, String message) implements IServerResponseInfo {

    /**
     * 创建一个包含错误消息的响应信息，使用默认的失败状态码。
     *
     * @param message 错误消息
     * @return IServerResponseInfo 的实例
     */
    public static IServerResponseInfo error(String message) {
        return new ServerResponseInfo(ServerResponseInfoEnum.CUSTOM_FAIL_MESSAGE.code(), message);
    }

    /**
     * 创建一个包含自定义状态码和消息的响应信息。
     *
     * @param code    自定义状态码
     * @param message 自定义错误消息
     * @return IServerResponseInfo 的实例
     */
    public static IServerResponseInfo error(String code, String message) {
        return new ServerResponseInfo(code, message);
    }
}
