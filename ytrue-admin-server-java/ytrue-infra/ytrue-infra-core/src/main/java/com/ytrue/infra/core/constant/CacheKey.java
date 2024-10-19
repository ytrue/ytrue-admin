package com.ytrue.infra.core.constant;

/**
 * CacheKey 接口用于定义缓存键的常量。
 *
 * <p>
 * 该接口集中管理应用中的缓存键，便于统一维护和使用。
 * </p>
 *
 * @author ytrue
 * @date 2023-11-25 0:06
 */
public interface CacheKey {

    /**
     * 默认缓存键，表示通用的默认值。
     */
    String CACHE_DEFAULT_KEY = "'default'";

    /**
     * 后台登录验证码的缓存键前缀。
     *
     * <p>
     * 该键用于存储后台登录时的验证码，以便于验证用户输入的验证码是否正确。
     * </p>
     */
    String ADMIN_LOGIN_CAPTCHA = "ADMIN_LOGIN_CAPTCHA:";
}
