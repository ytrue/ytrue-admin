package com.ytrue.infra.storage.enums;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description FileStoragePlatformEnum
 */
public enum StorageType {

    /**
     * 阿里云
     */
    oss,
    /**
     * 本地
     */
    local,
    /**
     * 七牛云
     */
    kodo,
    /**
     * 腾讯云
     */
    cos;
}
