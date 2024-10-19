package com.ytrue.infra.operationlog.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2022/6/7 17:39
 * @description OperateLogProperties
 */
@Data
@ConfigurationProperties(prefix = "ytrue.log")
public class OperateLogProperties {

    private Boolean enabled = true;
}
