package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class AliyunOssStorageProperties extends BaseStorageProperties {

    private String bucket = "";

    private String accessKeyId = "";

    private String accessKeySecret = "";

    private String endPoint = "";

    private String domain = "";
}
