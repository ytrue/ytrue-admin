package com.ytrue.infra.mysql.query;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置属性类，用于定义与查询相关的设置。
 *
 * @author ytrue
 * @date 2023-09-09 16:36
 */
@Data
@ConfigurationProperties(prefix = "ytrue.query")
public class QueryProperties {

    /**
     * 是否启用消息队列查询限制。
     * 默认值为 true，表示启用限制。
     */
    private boolean mqQueryLimitEnable = true;
}
