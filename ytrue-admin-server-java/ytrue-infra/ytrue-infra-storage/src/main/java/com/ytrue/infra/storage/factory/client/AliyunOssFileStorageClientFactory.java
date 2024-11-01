package com.ytrue.infra.storage.factory.client;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ytrue.infra.storage.properties.AliyunOssStorageProperties;
import lombok.Getter;

/**
 * 阿里云 OSS 文件存储客户端工厂实现。
 * 此类负责创建和管理阿里云 OSS 客户端实例。
 *
 * @author ytrue
 * @date 2024/05/23
 */
public class AliyunOssFileStorageClientFactory implements FileStorageClientFactory<OSS> {


    @Getter
    private final AliyunOssStorageProperties config;  // 阿里云 OSS 存储配置

    private volatile OSS client;  // 阿里云 OSS 客户端实例

    /**
     * 带参数的构造函数，用于初始化阿里云 OSS 存储配置。
     *
     * @param config 阿里云 OSS 存储的配置信息，包括 endpoint、accessKeyId 和 accessKeySecret
     */
    public AliyunOssFileStorageClientFactory(AliyunOssStorageProperties config) {
        this.config = config;
    }

    /**
     * 获取阿里云 OSS 客户端实例。
     * 该方法实现了懒加载机制，确保客户端在首次调用时创建，
     * 并在多线程环境中保持线程安全。
     *
     * @return 返回阿里云 OSS 客户端实例
     */
    @Override
    public OSS getClient() {
        // 使用双重检查锁定和静态内部类来确保线程安全和延迟加载
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
                }
            }
        }
        return client;
    }

    /**
     * 释放客户端资源。
     * 在关闭工厂时调用此方法以确保客户端正常关闭。
     */
    @Override
    public void close() {
        if (client != null) {
            client.shutdown();  // 关闭 OSS 客户端
        }
    }
}
