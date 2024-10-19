package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseInfo;

/**
 * ServerResponseInfoEnum 枚举类用于定义标准的服务器响应状态码和对应的消息信息。
 * 该枚举实现了 IServerResponseInfo 接口，以提供状态码和消息的统一访问方式。
 */
public enum ServerResponseInfoEnum implements IServerResponseInfo {
    // 状态码
    OK("200", "成功"),

    //-----------------------------------------------4xx
    NOT_FOUND("404", "数据不存在"),
    BAD_REQUEST("400", "参数错误"),
    UNAUTHORIZED("401", "未认证"),
    FORBIDDEN("403", "权限不足"),

    // ---------------------------------------------5xxx
    ILLEGAL_OPERATION("501", "非法操作"),
    INTERNAL_SERVER_ERROR("500", "服务器出了点小差"),
    CUSTOM_FAIL_MESSAGE("500", "");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 前端进行页面展示的信息
     */
    private final String message;

    /**
     * 构造方法，用于初始化状态码和消息信息。
     *
     * @param code    状态码
     * @param message 状态对应的描述信息
     */
    ServerResponseInfoEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
