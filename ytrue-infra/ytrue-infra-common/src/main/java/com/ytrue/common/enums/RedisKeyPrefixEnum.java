package com.ytrue.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @date 2023-06-02 15:22
 * @description RedisKeyPrefixEnum
 */
@AllArgsConstructor
@Getter
public enum RedisKeyPrefixEnum {

    /**
     * 后台登录验证码
     */
    ADMIN_LOGIN_CAPTCHA("ADMIN_LOGIN_CAPTCHA:");

    public final String value;
}
