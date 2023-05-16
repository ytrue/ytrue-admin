package com.ytrue.tools.storage;

import com.ytrue.tools.storage.platform.AliyunOssStorage;
import com.ytrue.tools.storage.platform.LocalStorage;
import com.ytrue.tools.storage.platform.QiniuKodoStorage;
import com.ytrue.tools.storage.platform.TencentCosStorage;
import com.ytrue.tools.storage.properties.AliyunOssStorageProperties;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import com.ytrue.tools.storage.properties.QiniuKodoStorageProperties;
import com.ytrue.tools.storage.properties.TencentCosStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(value = {
        AliyunOssStorageProperties.class,
        LocalStorageProperties.class,
        TencentCosStorageProperties.class,
        QiniuKodoStorageProperties.class,
})
public class StorageAutoConfiguration {
    @Bean
    public AliyunOssStorage aliyunOssStorage(AliyunOssStorageProperties aliyunOssStorageProperties) {
        return new AliyunOssStorage(aliyunOssStorageProperties);
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
