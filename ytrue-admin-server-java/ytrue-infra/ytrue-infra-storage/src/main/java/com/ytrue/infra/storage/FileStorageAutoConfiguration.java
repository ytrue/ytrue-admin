package com.ytrue.infra.storage;

import com.ytrue.infra.storage.factory.platform.AliyunOssFileStoragePlatformFactory;
import com.ytrue.infra.storage.factory.platform.QiniuKodoFileStoragePlatformFactory;
import com.ytrue.infra.storage.factory.platform.TencentCosFileStoragePlatformFactory;
import com.ytrue.infra.storage.properties.StoragePropertiesProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 文件存储自动配置类。
 * 当配置属性 "ytrue.storage.enabled" 为 true 时，此配置类将生效。
 *
 * @author ytrue
 * @description: FileStorageAutoConfiguration
 * @date 2024/10/30 15:00
 */
@ConditionalOnProperty(name = "ytrue.storage.enabled", havingValue = "true")
@EnableConfigurationProperties(StoragePropertiesProvider.class)
public class FileStorageAutoConfiguration {

    /**
     * 创建 FileStorageSmartInitializingSingleton 的 Bean。
     *
     * @param applicationContext Spring 应用上下文
     * @return FileStorageSmartInitializingSingleton 实例
     */
    @Bean
    public FileStorageSmartInitializingSingleton fileStorageSmartInitializingSingleton(ApplicationContext applicationContext) {
        return new FileStorageSmartInitializingSingleton(applicationContext);
    }

    /**
     * 创建 AliyunOssFileStoragePlatformFactory 的 Bean。
     *
     * @return AliyunOssFileStoragePlatformFactory 实例
     */
    @Bean
    public AliyunOssFileStoragePlatformFactory aliyunOssFileStoragePlatformFactory() {
        return new AliyunOssFileStoragePlatformFactory();
    }

    /**
     * 创建 TencentCosFileStoragePlatformFactory 的 Bean。
     *
     * @return TencentCosFileStoragePlatformFactory 实例
     */
    @Bean
    public TencentCosFileStoragePlatformFactory tencentCosFileStoragePlatformFactory() {
        return new TencentCosFileStoragePlatformFactory();
    }

    /**
     * 创建 QiniuKodoFileStoragePlatformFactory 的 Bean。
     *
     * @return QiniuKodoFileStoragePlatformFactory 实例
     */
    @Bean
    public QiniuKodoFileStoragePlatformFactory qiniuKodoFileStoragePlatformFactory() {
        return new QiniuKodoFileStoragePlatformFactory();
    }
}
