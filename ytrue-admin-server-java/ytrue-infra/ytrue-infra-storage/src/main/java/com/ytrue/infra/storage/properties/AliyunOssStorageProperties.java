package com.ytrue.infra.storage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云 OSS 存储配置类。
 * 用于映射配置文件中以 "ytrue.storage.oss" 为前缀的属性。
 *
 * @author ytrue
 * @date 2023/4/25
 */
@ConfigurationProperties(prefix = "ytrue.storage.oss")
@Data
public class AliyunOssStorageProperties implements IStorageProperties {

    /**
     * OSS 存储桶名称。
     * 该字段指定了文件上传到的存储桶。
     */
    private String bucket;

    /**
     * 阿里云访问密钥 ID。
     * 用于进行身份验证和授权。
     */
    private String accessKeyId;

    /**
     * 阿里云访问密钥 Secret。
     * 用于进行身份验证和授权。
     */
    private String accessKeySecret;

    /**
     * OSS 服务的访问端点。
     * 该字段指定了 OSS 服务的具体地址。
     */
    private String endPoint;

    /**
     * 自定义域名。
     * 该字段用于设置访问文件的自定义域名（如果有的话）。
     */
    private String domain;

    /**
     * 文件存储主机。
     * 该字段可以用于设置文件的主机名或路径前缀。
     */
    private String fileHost;
}
