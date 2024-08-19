package com.ytrue.infra.security.integration.authenticator;


import com.ytrue.infra.security.integration.IntegrationAuthenticationEntity;

/**
 * @author ytrue
 * @date 2022/2/28 12:47
 * @description 集成认证-认证器抽象类
 */
public abstract class AbstractPreparableIntegrationAuthenticator implements IntegrationAuthenticator {

    /**
     * 预处理
     *
     * @param entity 集成认证实体
     */
    @Override
    public void prepare(IntegrationAuthenticationEntity entity) {

    }

    /**
     * 认证结束后执行
     *
     * @param entity 集成认证实体
     */
    @Override
    public void complete(IntegrationAuthenticationEntity entity) {

    }
}
