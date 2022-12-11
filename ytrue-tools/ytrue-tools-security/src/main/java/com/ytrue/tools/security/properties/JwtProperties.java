package com.ytrue.tools.security.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2022/5/10 17:11
 * @description 配置属性类，用于封装yml配置文件中关于接口文档相关的配置信息
 */

@Data
@ConfigurationProperties(prefix = "ytrue.security.jwt")
public class JwtProperties {

    /**
     * Token过期时间必须大于生效时间
     */
    private Long tokenExpireTime = 30 * 60 * 1000L;

    /**
     * Token加密解密的密码
     */
    private String tokenSecret = "pwd";

    /**
     * 加密类型 三个值可取 HS256  HS384  HS512
     */
    private String jwtAlg = "HS256";

    /**
     * 添加一个前缀
     */
    private String jwtSeparator = "Bearer#";

    /**
     * token生效时间(默认是从当前开始生效)
     * 默认：new Date(System.currentTimeMillis() + START_TIME)
     */
    private Long startTime = 0L;

    /**
     * token在什么时间之前是不可用的（默认从当前时间）
     * 默认：new Date(System.currentTimeMillis() + BEFORE_TIME)
     */
    private Long beforeTime = 0L;
}
