package com.ytrue.infra.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/5/10 17:27
 * @description 忽略认证接口
 */
@Data
@ConfigurationProperties(prefix = "ytrue.security.ignoring")
public class IgnoreWebSecurityProperties {
    /**
     * 需要忽略的 URL 格式，不考虑请求方法
     */
    private Set<String> pattern = new HashSet<>();

    /**
     * 需要忽略的 GET 请求
     */
    private Set<String> get = new HashSet<>();

    /**
     * 需要忽略的 POST 请求
     */
    private Set<String> post = new HashSet<>();

    /**
     * 需要忽略的 DELETE 请求
     */
    private Set<String> delete = new HashSet<>();

    /**
     * 需要忽略的 PUT 请求
     */
    private Set<String> put = new HashSet<>();

    /**
     * 需要忽略的 HEAD 请求
     */
    private Set<String> head = new HashSet<>();

    /**
     * 需要忽略的 PATCH 请求
     */
    private Set<String> patch = new HashSet<>();

    /**
     * 需要忽略的 OPTIONS 请求
     */
    private Set<String> options = new HashSet<>();

    /**
     * 需要忽略的 TRACE 请求
     */
    private Set<String> trace = new HashSet<>();
}
