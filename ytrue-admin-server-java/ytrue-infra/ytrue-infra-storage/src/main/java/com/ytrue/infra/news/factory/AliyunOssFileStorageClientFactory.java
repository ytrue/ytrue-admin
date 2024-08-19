package com.ytrue.infra.news.factory;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ytrue.infra.storage.properties.AliyunOssStorageProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ytrue
 * @date 2024/5/23 18:05
 * @description AliyunOssFileStorageClientFactory
 */
@NoArgsConstructor
public class AliyunOssFileStorageClientFactory implements FileStorageClientFactory<OSS> {

    @Getter
    private AliyunOssStorageProperties config;

    private volatile OSS client;

    public AliyunOssFileStorageClientFactory(AliyunOssStorageProperties config) {
        this.config = config;
    }

    public OSS getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
                }
            }
        }
        return client;
    }
}
