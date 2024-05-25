package com.ytrue.infra.security.integration;

/**
 * @author ytrue
 * @date 2022/2/28 12:47
 * @description 集成认证上下文
 */
public class IntegrationAuthenticationContext {

    private static final ThreadLocal<IntegrationAuthenticationEntity> HOLDERS = new ThreadLocal<>();

    /**
     * 设置
     *
     * @param entity
     */
    public static void set(IntegrationAuthenticationEntity entity) {
        HOLDERS.set(entity);
    }

    /**
     * 获取
     *
     * @return {@link IntegrationAuthenticationEntity}
     */
    public static IntegrationAuthenticationEntity get() {
        return HOLDERS.get();
    }

    /**
     * 删除
     */
    public static void clear() {
        HOLDERS.remove();
    }
}
