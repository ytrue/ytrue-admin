package com.ytrue.infra.storage.factory.platform;

import com.ytrue.infra.storage.platform.FileStorage;
import com.ytrue.infra.storage.properties.BaseStorageProperties;

/**
 * 表示用于创建不同存储平台的 FileStorage 实例的工厂接口。
 *
 * @param <T> 用于文件存储实例的配置属性类型。
 *
 *            <p>该接口定义了获取存储属性类、创建新的文件存储实例以及检索平台名称的方法。</p>
 * @author ytrue
 * @date 2024/11/1
 */
public interface FileStoragePlatformFactory<T extends BaseStorageProperties> {

    /**
     * 获取该工厂使用的存储属性的类类型。
     *
     * @return 存储属性的类。
     */
    Class<T> getStoragePropertiesClass();

    /**
     * 使用提供的配置创建新的 FileStorage 实例。
     *
     * @param config 文件存储实例的配置属性。
     * @return 新创建的 FileStorage 实例。
     */
    FileStorage createInstance(T config);

    /**
     * 获取与该工厂相关联的平台名称。
     *
     * @return 平台名称字符串。
     */
    String platform();
}
