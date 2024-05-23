package com.ytrue.infra.news.factory;

/**
 * @author ytrue
 * @date 2024/5/23 18:05
 * @description FileStorageClientFactory
 */
public interface FileStorageClientFactory<T> extends AutoCloseable {
    /**
     * 获取客户端
     *
     * @return
     */
    T getClient();


    /**
     * 释放相关资源
     */
    @Override
    default void close() {
    }
}
