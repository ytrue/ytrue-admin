package com.ytrue.infra.news.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description AliyunOssStorageProperties
 */
@ConfigurationProperties(prefix = "ytrue.storage.oss")
@Data
public class AliyunOssStorageProperties implements IStorageProperties {

    private String bucket;

    private String accessKeyId;

    private String accessKeySecret;

    private String endPoint;

    private String domain;

    private String fileHost;
}
