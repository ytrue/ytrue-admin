package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description QiniuKodoStorageProperties
 */
@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class QiniuKodoStorageProperties extends BaseStorageProperties {

    private String bucket = "";

    private String accessKey = "";

    private String secretKey = "";

    private String domain = "";

    private String fileHost = "";
}
