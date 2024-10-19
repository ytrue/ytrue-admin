package com.ytrue.infra.core.constant;

/**
 * @author ytrue
 * @date 2023-06-02 15:27
 * @description StrPool
 */
public interface StrPool extends cn.hutool.core.text.StrPool {

    String EMPTY_STRING = "";

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "111111";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * www主域
     */
    String WWW = "www.";
}
