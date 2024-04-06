package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseCode;

public enum ResponseCodeEnum implements IServerResponseCode {
    //状态码
    SUCCESS("2000", "成功"),

    FAIL("5000", "服务器出了点小差"),
    CUSTOM_FAIL_MESSAGE("5000", ""),
    DATA_NOT_FOUND("5004", "数据不存在"),
    ILLEGAL_OPERATION("5005", "非法操作");

    /**
     * code错误码
     */
    private final String code;

    /**
     * 前端进行页面展示的信息
     */
    private final String message;

    ResponseCodeEnum(String code, String message) {
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
