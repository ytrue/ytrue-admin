package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ytrue.storage.kodo")
@Data
@EqualsAndHashCode(callSuper = true)
public class QiniuKodoStorageProperties extends BaseStorageProperties {

    private String bucket = "";

    private String accessKey = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
