package com.ytrue.infra.storage.factory.platform;

import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
import com.ytrue.infra.storage.platform.AliyunOssFileStorage;
import com.ytrue.infra.storage.platform.FileStorage;
import com.ytrue.infra.storage.properties.AliyunOssStorageProperties;

/**
 * @author ytrue
 * @description: AliyunOssFileStoragePlatformFactory 负责创建 AliyunOssFileStorage 实例的工厂类。
 * @date 2024/11/1 10:34
 */
public class AliyunOssFileStoragePlatformFactory implements FileStoragePlatformFactory<AliyunOssStorageProperties> {

    /**
     * 获取存储属性类的类型。
     *
     * @return AliyunOssStorageProperties 的 Class 对象。
     */
    @Override
    public Class<AliyunOssStorageProperties> getStoragePropertiesClass() {
        return AliyunOssStorageProperties.class;
    }

    /**
     * 创建一个新的 AliyunOssFileStorage 实例。
     *
     * @param config 用于初始化 AliyunOssFileStorage 的配置对象。
     * @return 新创建的 FileStorage 实例。
     */
    @Override
    public FileStorage createInstance(AliyunOssStorageProperties config) {
        return new AliyunOssFileStorage(config);
    }

    /**
     * 返回当前平台的名称。
     *
     * @return 当前平台的名称，格式为小写。
     */
    @Override
    public String platform() {
        return FileStoragePlatformEnum.OSS.name().toLowerCase();
    }
}
