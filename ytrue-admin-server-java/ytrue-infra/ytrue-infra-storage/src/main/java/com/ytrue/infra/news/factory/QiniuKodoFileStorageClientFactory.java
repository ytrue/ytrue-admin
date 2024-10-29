package com.ytrue.infra.news.factory;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ytrue.infra.storage.properties.QiniuKodoStorageProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 七牛云 Kodo 文件存储客户端工厂实现。
 * 此类负责创建和管理七牛云 Kodo 客户端实例。
 *
 * @author ytrue
 * @date 2024/05/23
 */
public class QiniuKodoFileStorageClientFactory implements FileStorageClientFactory<QiniuKodoFileStorageClientFactory.QiniuKodoClient> {

    @Getter
    private final QiniuKodoStorageProperties config;  // 七牛云 Kodo 存储配置

    private volatile QiniuKodoClient client;  // 七牛云 Kodo 客户端实例

    /**
     * 带参数的构造函数，用于初始化七牛云 Kodo 存储配置。
     *
     * @param config 七牛云 Kodo 存储的配置信息，包括 Access Key 和 Secret Key
     */
    public QiniuKodoFileStorageClientFactory(QiniuKodoStorageProperties config) {
        this.config = config;
    }

    /**
     * 获取七牛云 Kodo 客户端实例。
     * 该方法实现了懒加载机制，确保客户端在首次调用时创建，
     * 并在多线程环境中保持线程安全。
     *
     * @return 返回七牛云 Kodo 客户端实例
     */
    @Override
    public QiniuKodoClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new QiniuKodoFileStorageClientFactory.QiniuKodoClient(config.getAccessKey(), config.getSecretKey());
                }
            }
        }
        return client;
    }

    /**
     * 七牛云 Kodo 客户端类。
     * 包含用于文件存储的各种管理器和认证信息。
     */
    @Getter
    @Setter
    public static class QiniuKodoClient {
        private String accessKey;   // 七牛云 Access Key
        private String secretKey;   // 七牛云 Secret Key
        private Auth auth;          // 七牛云认证对象
        private BucketManager bucketManager; // 七牛云存储桶管理器
        private UploadManager uploadManager; // 七牛云上传管理器

        /**
         * 带参数的构造函数，用于初始化七牛云 Kodo 客户端。
         *
         * @param accessKey 七牛云 Access Key
         * @param secretKey 七牛云 Secret Key
         */
        public QiniuKodoClient(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        /**
         * 获取七牛云认证对象。
         *
         * @return 七牛云认证对象
         */
        public Auth getAuth() {
            if (auth == null) {
                auth = Auth.create(accessKey, secretKey);
            }
            return auth;
        }

        /**
         * 获取七牛云存储桶管理器。
         *
         * @return 七牛云存储桶管理器实例
         */
        public BucketManager getBucketManager() {
            if (bucketManager == null) {
                bucketManager = new BucketManager(getAuth(), new Configuration(Region.autoRegion()));
            }
            return bucketManager;
        }

        /**
         * 获取七牛云上传管理器。
         *
         * @return 七牛云上传管理器实例
         */
        public UploadManager getUploadManager() {
            if (uploadManager == null) {
                uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
            }
            return uploadManager;
        }
    }
}
