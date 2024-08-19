package com.ytrue.infra.news.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description TencentCosStorageProperties
 */
@ConfigurationProperties(prefix = "ytrue.storage.cos")
@Data
public class TencentCosStorageProperties implements IStorageProperties {
    private String bucket = "";

    private String region = "";

    private String secretId = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
