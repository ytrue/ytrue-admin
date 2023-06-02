package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description LocalStorageProperties
 */
@ConfigurationProperties(prefix = "ytrue.storage.local")
@Data
public class LocalStorageProperties implements IStorageProperties {

    /**
     * 本地存储路径
     */
    private String fileHost = "storage";

    /**
     * 访问域名
     */
    private String domain = "http://127.0.0.1:7000";

}
