package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ytrue.storage.oss")
@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunOssStorageProperties extends BaseStorageProperties {

    private String bucket;

    private String accessKeyId;

    private String accessKeySecret;

    private String endPoint;

    private String domain;

    private String fileHost;
}
