package com.ytrue.infra.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * AliyunOssStorageProperties 是阿里云 OSS 存储的配置类，继承自 BaseStorageProperties。
 * 包含了阿里云特有的属性，如 Access Key ID、Access Key Secret 和 Endpoint。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class AliyunOssStorageProperties extends BaseStorageProperties {

    private String bucket;

    private String accessKeyId;

    private String accessKeySecret;

    private String endPoint;


}
