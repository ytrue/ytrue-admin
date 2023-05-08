package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;


/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description TencentCosStorageProperties
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class TencentCosStorageProperties extends BaseStorageProperties {
    private String bucket = "";

    private String region = "";

    private String secretId = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "test/";
}
