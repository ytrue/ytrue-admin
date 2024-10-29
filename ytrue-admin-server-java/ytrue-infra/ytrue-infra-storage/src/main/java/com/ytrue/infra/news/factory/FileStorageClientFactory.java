package com.ytrue.infra.news.factory;

/**
 * 文件存储客户端工厂接口。
 * 该接口定义了获取文件存储客户端的标准方法，并支持资源的释放。
 *
 * @param <T> 客户端类型
 * @author ytrue
 * @date 2024/05/23
 */
public interface FileStorageClientFactory<T> extends AutoCloseable {

    /**
     * 获取文件存储客户端实例。
     *
     * @return 返回一个文件存储客户端实例
     */
    T getClient();

    /**
     * 释放相关资源。
     * 该方法用于关闭客户端和释放任何与其关联的资源。
     * 默认实现为空，可以根据需要覆盖。
     */
    @Override
    default void close() {
    }
}
