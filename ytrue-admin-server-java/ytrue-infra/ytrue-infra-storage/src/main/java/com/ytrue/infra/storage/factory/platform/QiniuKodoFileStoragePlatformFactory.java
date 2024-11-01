package com.ytrue.infra.storage.factory.platform;

import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
import com.ytrue.infra.storage.platform.FileStorage;
import com.ytrue.infra.storage.platform.QiniuKodoFileStorage;
import com.ytrue.infra.storage.properties.QiniuKodoStorageProperties;

/**
 * @author ytrue
 * @description: QiniuKodoFileStoragePlatformFactory 负责创建 QiniuKodoFileStorage 实例的工厂类。
 * @date 2024/11/1 10:34
 */
public class QiniuKodoFileStoragePlatformFactory implements FileStoragePlatformFactory<QiniuKodoStorageProperties> {

    /**
     * 获取存储属性类的类型。
     *
     * @return QiniuKodoStorageProperties 的 Class 对象。
     */
    @Override
    public Class<QiniuKodoStorageProperties> getStoragePropertiesClass() {
        return QiniuKodoStorageProperties.class;
    }

    /**
     * 创建一个新的 QiniuKodoFileStorage 实例。
     *
     * @param config 用于初始化 QiniuKodoFileStorage 的配置对象。
     * @return 新创建的 FileStorage 实例。
     */
    @Override
    public FileStorage createInstance(QiniuKodoStorageProperties config) {
        return new QiniuKodoFileStorage(config);
    }

    /**
     * 返回当前平台的名称。
     *
     * @return 当前平台的名称，格式为小写。
     */
    @Override
    public String platform() {
        return FileStoragePlatformEnum.KODO.name().toLowerCase();
    }
}
