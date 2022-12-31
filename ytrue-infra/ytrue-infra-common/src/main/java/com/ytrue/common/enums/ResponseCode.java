package com.ytrue.common.enums;

import com.ytrue.common.base.IBaseExceptionCode;

/**
 * @author ytrue
 * @date 2022/4/21 17:05
 * @description 响应状态枚举
 * <p>
 * 枚举的值都是固定的，且是全局唯一的，
 * 用 java 的术语来说就是单例的，
 * 所以我们写枚举类时一般都不会给自定义属性写Setter方法。
 * 本着单例思想，我们还是加上final。
 */
public enum ResponseCode implements IBaseExceptionCode {

    //状态码
    SUCCESS(2000, "成功"),

    NO_LOGIN(4001, "未登录"),
    INVALID_TOKEN(4001, "无效token"),
    PERMISSION_DENIED(4003, "权限不足"),
    ACCOUNT_NOT_FOUND(4004, "账号不存在"),
    ACCOUNT_PASSWORD_INCORRECT(4005, "账号或者密码不正确"),



    UNKNOWN(5001, "未知异常"),
    FAIL(5000, "服务器内部错误"),
    DATA_NOT_FOUND(5004, "数据不存在"),


    SCHEDULED_TASK_EXISTS(6001, "定时任务已存在"),
    JOB_EXISTS(6002, "岗位已存在"),
    ACCOUNT_EXISTS(6003, "账号已存在"),
    HAS_CHILD(6004, "存在子级，请解除后再试"),
    PARENT_EQ_ITSELF(6005, "父级不能是自己"),
    HAS_USER_ASSOCIATION(6006, "存在用户关联，请解除后再试"),
    HAS_ROLE_ASSOCIATION(6017, "存在角色关联，请解除后再试"),

    OLD_PASSWORD_ERROR(6018, "旧密码错误"),
    NEW_PASS_NOT_EQUAL_OLD_PASS(6019, "新密码不能与旧密码相同");


    /**
     * code错误码
     */
    private final Integer code;

    /**
     * 前端进行页面展示的信息
     */
    private final String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
