package com.ytrue.infra.storage;

import com.ytrue.infra.storage.platform.FileStorage;
import org.springframework.beans.factory.FactoryBean;

/**
 * FileStorageFactoryBean 的实现，用于创建和管理 FileStorage 实例。
 * 此类允许 Spring 将 FileStorage 视为一个 bean，提供额外的配置和生命周期管理功能。
 *
 * @author ytrue
 * @description FileStorageFactoryBean 负责生成 FileStorage bean。
 * @date 2024/11/1 15:10
 */
public class FileStorageFactoryBean implements FactoryBean<FileStorage> {

    // 需要管理的 FileStorage 实例
    private final FileStorage fileStorage;

    /**
     * FileStorageFactoryBean 的构造函数。
     *
     * @param fileStorage 要管理的 FileStorage 实例。
     */
    public FileStorageFactoryBean(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    /**
     * 返回管理的 FileStorage 实例。
     *
     * @return 管理的 FileStorage 实例。
     * @throws Exception 如果获取对象时出现问题。
     */
    @Override
    public FileStorage getObject() throws Exception {
        return fileStorage;
    }

    /**
     * 返回此 FactoryBean 管理的对象类型。
     *
     * @return 管理的 FileStorage 实例的类类型。
     */
    @Override
    public Class<?> getObjectType() {
        return fileStorage.getClass();
    }

    /**
     * 指示该 bean 是否是单例或原型。
     *
     * @return 如果是单例，则返回 true；否则返回 false。
     */
    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
