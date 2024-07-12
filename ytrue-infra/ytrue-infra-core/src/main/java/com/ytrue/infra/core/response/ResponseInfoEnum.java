package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseInfo;

public enum ResponseInfoEnum implements IServerResponseInfo {
    //状态码
    SUCCESS("200", "成功"),

    FAIL("500", "服务器出了点小差"),
    CUSTOM_FAIL_MESSAGE("500", ""),
    DATA_NOT_FOUND("404", "数据不存在"),
    ILLEGAL_OPERATION("501", "非法操作");

    /**
     * code错误码
     */
    private final String code;

    /**
     * 前端进行页面展示的信息
     */
    private final String message;

    ResponseInfoEnum(String code, String message) {
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
