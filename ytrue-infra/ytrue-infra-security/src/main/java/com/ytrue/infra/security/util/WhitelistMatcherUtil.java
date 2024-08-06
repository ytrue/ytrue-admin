package com.ytrue.infra.security.util;

import cn.hutool.core.text.AntPathMatcher;
import com.ytrue.infra.security.SecurityAutoConfiguration;
import com.ytrue.infra.security.constant.HttpRequestType;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.Map;

public class WhitelistMatcherUtil {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 检查请求路径是否在白名单中。
     *
     * @return 如果请求路径在白名单中则返回 true，否则返回 false
     */
    public static boolean isPathWhitelisted(HttpServletRequest request) {
        // 获取白名单配置
        Map<String, HashSet<String>> ignoreAuthConfigMap = SecurityAutoConfiguration.getIgnoreAuthConfigMap();
        // 获取全部路径白名单配置
        HashSet<String> ignoreAuthPaths = ignoreAuthConfigMap.get(HttpRequestType.PATTERN);
        // 获取方法特定的路径白名单配置，并合并到全局白名单
        HashSet<String> methodSpecificPaths = ignoreAuthConfigMap.get(request.getMethod().toUpperCase());
        ignoreAuthPaths.addAll(methodSpecificPaths);
        // 检查请求路径是否匹配任意白名单路径
        return ignoreAuthPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
    }


}
