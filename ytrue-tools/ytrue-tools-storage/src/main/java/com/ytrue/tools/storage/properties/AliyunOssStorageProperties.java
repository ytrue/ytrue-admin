package com.ytrue.tools.storage.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AliyunOssStorageProperties extends BaseStorageProperties {

    private String bucket = "";

    private String accessKeyId = "";

    private String accessKeySecret = "";

    private String endPoint = "";

    private String domain = "";

    private String fileHost = "";
}
