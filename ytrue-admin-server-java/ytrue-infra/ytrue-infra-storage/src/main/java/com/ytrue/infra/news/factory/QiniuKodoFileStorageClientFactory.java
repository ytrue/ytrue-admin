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
 * @author ytrue
 * @date 2024/5/23 18:05
 * @description QiniuKodoFileStorageClientFactory
 */
public class QiniuKodoFileStorageClientFactory implements FileStorageClientFactory<QiniuKodoFileStorageClientFactory.QiniuKodoClient> {

    @Getter
    private QiniuKodoStorageProperties config;

    private volatile QiniuKodoClient client;

    public QiniuKodoFileStorageClientFactory(QiniuKodoStorageProperties config) {
        this.config = config;
    }

    @Override
    public QiniuKodoClient getClient() {
        if (client == null) {
            synchronized (this) {
                client = new QiniuKodoFileStorageClientFactory.QiniuKodoClient(config.getAccessKey(), config.getSecretKey());
            }
        }
        return client;
    }

    @Getter
    @Setter
    public static class QiniuKodoClient {
        private String accessKey;
        private String secretKey;
        private Auth auth;
        private BucketManager bucketManager;
        private UploadManager uploadManager;

        public QiniuKodoClient(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        public Auth getAuth() {
            if (auth == null) {
                auth = Auth.create(accessKey, secretKey);
            }
            return auth;
        }

        public BucketManager getBucketManager() {
            if (bucketManager == null) {
                bucketManager = new BucketManager(getAuth(), new Configuration(Region.autoRegion()));
            }
            return bucketManager;
        }

        public UploadManager getUploadManager() {
            if (uploadManager == null) {
                uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
            }
            return uploadManager;
        }
    }
}
