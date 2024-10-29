package com.ytrue.infra.news.enums;

/**
 * 文件存储平台枚举。
 * 此枚举定义了可用的文件存储平台选项。
 *
 * @author ytrue
 * @date 2023/04/25
 */
public enum FileStoragePlatformEnum {

    /**
     * 阿里云 OSS (Object Storage Service)
     * 用于存储和管理大量数据的云服务。
     */
    OSS,

    /**
     * 本地存储
     * 文件存储在本地磁盘或服务器上。
     */
    LOCAL,

    /**
     * 七牛云 KODO
     * 七牛云提供的对象存储服务，适用于海量数据存储和处理。
     */
    KODO,

    /**
     * 腾讯云 COS (Cloud Object Storage)
     * 腾讯云的对象存储服务，支持海量数据存储和高效的数据管理。
     */
    COS;
}
