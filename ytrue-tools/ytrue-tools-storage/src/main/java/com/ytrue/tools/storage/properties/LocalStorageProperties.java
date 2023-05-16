package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ytrue.storage.local")
@Data
@EqualsAndHashCode(callSuper = true)
public class LocalStorageProperties extends BaseStorageProperties {

    /**
     * 本地存储路径
     */
    private String fileHost = "storage";

    /**
     * 访问域名
     */
    private String domain = "http://127.0.0.1:7000";

}
