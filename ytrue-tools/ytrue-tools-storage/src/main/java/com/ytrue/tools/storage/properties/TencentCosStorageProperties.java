package com.ytrue.tools.storage.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TencentCosStorageProperties extends BaseStorageProperties {
    private String bucket = "";

    private String region = "";

    private String secretId = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
