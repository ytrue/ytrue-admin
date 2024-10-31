package com.ytrue.infra.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description QiniuKodoStorageProperties
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class QiniuKodoStorageProperties extends BaseStorageProperties {

    private String bucket;

    private String accessKey;

    private String secretKey;
}
