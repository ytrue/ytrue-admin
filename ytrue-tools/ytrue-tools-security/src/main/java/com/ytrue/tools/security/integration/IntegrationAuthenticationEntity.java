package com.ytrue.tools.security.integration;

import lombok.Data;

import java.util.Map;

/**
 * @author ytrue
 * @date 2021/2/28 12:47
 * @description 集成认证实体
 */
@Data
public class IntegrationAuthenticationEntity {

    /**
     * 请求登录认证类型
     */
    private String authType;

    /**
     * 请求登录认证参数集合
     */
    private Map<String, String[]> authParameters;

    /**
     * 获取认证参数
     *
     * @param parameter
     * @return {@link String}
     */
    public String getAuthParameter(String parameter) {
        String[] values = this.authParameters.get(parameter);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }
}
