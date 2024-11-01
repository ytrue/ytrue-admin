package com.ytrue.infra.storage.factory.platform;

import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
import com.ytrue.infra.storage.platform.FileStorage;
import com.ytrue.infra.storage.platform.TencentCosFileStorage;
import com.ytrue.infra.storage.properties.TencentCosStorageProperties;

/**
 * @author ytrue
 * @description: TencentCosFileStoragePlatformFactory 负责创建 TencentCosFileStorage 实例的工厂类。
 * @date 2024/11/1 10:34
 */
public class TencentCosFileStoragePlatformFactory implements FileStoragePlatformFactory<TencentCosStorageProperties> {

    /**
     * 获取存储属性类的类型。
     *
     * @return TencentCosStorageProperties 的 Class 对象。
     */
    @Override
    public Class<TencentCosStorageProperties> getStoragePropertiesClass() {
        return TencentCosStorageProperties.class;
    }

    /**
     * 创建一个新的 TencentCosFileStorage 实例。
     *
     * @param config 用于初始化 TencentCosFileStorage 的配置对象。
     * @return 新创建的 FileStorage 实例。
     */
    @Override
    public FileStorage createInstance(TencentCosStorageProperties config) {
        return new TencentCosFileStorage(config);
    }

    /**
     * 返回当前平台的名称。
     *
     * @return 当前平台的名称，格式为小写。
     */
    @Override
    public String platform() {
        return FileStoragePlatformEnum.COS.name().toLowerCase();
    }
}
