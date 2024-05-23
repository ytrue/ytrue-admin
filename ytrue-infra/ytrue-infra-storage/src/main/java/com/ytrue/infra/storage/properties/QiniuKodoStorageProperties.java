package com.ytrue.infra.storage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description QiniuKodoStorageProperties
 */
@ConfigurationProperties(prefix = "ytrue.storage.kodo")
@Data
public class QiniuKodoStorageProperties implements IStorageProperties {

    private String bucket = "";

    private String accessKey = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
