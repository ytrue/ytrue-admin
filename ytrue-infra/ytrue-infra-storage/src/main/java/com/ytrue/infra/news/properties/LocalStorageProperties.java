package com.ytrue.infra.news.properties;

import lombok.Data;
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
    private String domain = "";

}
