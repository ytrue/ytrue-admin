package com.ytrue.tools.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2022/6/7 17:39
 * @description LogProperties
 */
@Data
@ConfigurationProperties(prefix = "ytrue.log")
public class LogProperties {

    private Boolean enabled = true;
}
