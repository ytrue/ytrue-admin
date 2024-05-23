package com.ytrue.infra.storage;

import com.ytrue.infra.storage.platform.AliYunOssStorage;
import com.ytrue.infra.storage.platform.LocalStorage;
import com.ytrue.infra.storage.platform.QiniuKodoStorage;
import com.ytrue.infra.storage.platform.TencentCosStorage;
import com.ytrue.infra.storage.properties.AliyunOssStorageProperties;
import com.ytrue.infra.storage.properties.LocalStorageProperties;
import com.ytrue.infra.storage.properties.QiniuKodoStorageProperties;
import com.ytrue.infra.storage.properties.TencentCosStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description StorageAutoConfiguration
 */
@EnableConfigurationProperties(value = {
        AliyunOssStorageProperties.class,
        LocalStorageProperties.class,
        TencentCosStorageProperties.class,
        QiniuKodoStorageProperties.class,
})
public class StorageAutoConfiguration {
    @Bean
    public AliYunOssStorage aliyunOssStorage(AliyunOssStorageProperties aliyunOssStorageProperties) {
        return new AliYunOssStorage(aliyunOssStorageProperties);
    }


    @Bean
    public LocalStorage localStorage(LocalStorageProperties localStorageProperties) {
        return new LocalStorage(localStorageProperties);
    }

    @Bean
    public TencentCosStorage tencentCosStorage(TencentCosStorageProperties tencentCosStorageProperties) {
        return new TencentCosStorage(tencentCosStorageProperties);
    }

    @Bean
    public QiniuKodoStorage qiniuKodoStorage(QiniuKodoStorageProperties qiniuKodoStorageProperties) {
        return new QiniuKodoStorage(qiniuKodoStorageProperties);
    }
}
