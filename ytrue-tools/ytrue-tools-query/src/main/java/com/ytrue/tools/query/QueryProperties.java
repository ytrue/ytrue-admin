package com.ytrue.tools.query;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2023-09-09 16:36
 * @description QueryProperties
 */
@Data
@ConfigurationProperties(prefix = "ytrue.query")
public class QueryProperties {

    private boolean mqQueryLimitEnable = true;
}
