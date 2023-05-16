package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class QiniuKodoStorageProperties extends BaseStorageProperties {

    private String bucket = "";

    private String accessKey = "";

    private String secretKey = "";

    private String domain = "";
}
