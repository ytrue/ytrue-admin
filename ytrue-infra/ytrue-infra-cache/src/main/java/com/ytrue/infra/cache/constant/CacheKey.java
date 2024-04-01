package com.ytrue.infra.cache.constant;

/**
 * @author ytrue
 * @date 2023-11-25 0:06
 * @description CacheKey
 */
public interface CacheKey {

    String CACHE_DEFAULT_KEY = "'default'";

    /**
     * 后台登录验证码
     */
    String ADMIN_LOGIN_CAPTCHA = "ADMIN_LOGIN_CAPTCHA:";
}
