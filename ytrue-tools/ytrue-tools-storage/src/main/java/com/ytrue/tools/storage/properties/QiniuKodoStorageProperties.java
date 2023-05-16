package com.ytrue.tools.storage.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class QiniuKodoStorageProperties extends BaseStorageProperties {

    private String bucket = "";

    private String accessKey = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
