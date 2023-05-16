package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ytrue.storage.cos")
@Data
@EqualsAndHashCode(callSuper = true)
public class TencentCosStorageProperties extends BaseStorageProperties {
    private String bucket = "";

    private String region = "";

    private String secretId = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
