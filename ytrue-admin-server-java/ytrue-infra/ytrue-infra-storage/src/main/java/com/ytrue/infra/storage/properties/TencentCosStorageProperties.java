package com.ytrue.infra.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description TencentCosStorageProperties
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class TencentCosStorageProperties extends BaseStorageProperties {
    private String bucket;

    private String region;

    private String secretId;

    private String secretKey;
}
