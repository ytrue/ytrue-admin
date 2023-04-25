package com.ytrue.tools.storage.platform;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description QiniuKodoStorage
 */
@Component
public class QiniuKodoStorage extends AbstractStorage {

    private final QiniuKodoStorageProperties config;

    @Autowired
    public QiniuKodoStorage(QiniuKodoStorageProperties config) {
        this.config = config;
    }

    private volatile QiniuKodoClient client;

    /**
     * 单例模式运行，不需要每次使用完再销毁了
     *
     * @return
     */
    public QiniuKodoClient getClient() {
        if (client == null) {
            synchronized (this) {
                client = new QiniuKodoClient(config.getAccessKey(), config.getSecretKey());
            }
        }
        return client;
    }


    @Override
    public FileInfo upload(UploadInfo uploadInfo) {
        FileInfo fileInfo = buildFileInfo(uploadInfo);
        fileInfo.setPlatform(platform());

        String newFileKey = config.getFileHost() + fileInfo.getPath() + fileInfo.getFileName();
        fileInfo.setBasePath(config.getFileHost());
        fileInfo.setUrl(config.getDomain() + newFileKey);

        try (InputStream in = uploadInfo.getFileWrapper().getInputStream()) {
            QiniuKodoClient client = getClient();
            UploadManager uploadManager = client.getUploadManager();
            String token = client.getAuth().uploadToken(config.getBucket());
            // 上传
            uploadManager.put(in, newFileKey, token, null, fileInfo.getContentType());
            return fileInfo;
        } catch (IOException e) {
            try {
                client.getBucketManager().delete(config.getBucket(), newFileKey);
            } catch (QiniuException ignored) {
            }
            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileInfo.getOriginalFilename(), e);
        }
    }




    @Override
    public boolean exists(FileInfo fileInfo) {
        BucketManager manager = getClient().getBucketManager();
        try {
            com.qiniu.storage.model.FileInfo stat = manager.stat(config.getBucket(), "test/6447818aab30cb6d6b11e68e.png");
            System.out.println(stat);
            if (stat != null && stat.md5 != null) return true;
        } catch (QiniuException e) {
            // 612 指定资源不存在或已被删除
            if (e.code() == 612) {
                return false;
            }
            throw new StorageRuntimeException("查询文件是否存在失败！" + e.code() + "，" + e.response.toString(), e);
        }
        return false;
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        BucketManager manager = getClient().getBucketManager();
        try {
            delete(manager, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFileName());
        } catch (QiniuException e) {
            throw new StorageRuntimeException("删除文件失败！" + e.code() + "，" + e.response.toString(), e);
        }
        return true;
    }

    private void delete(BucketManager manager, String filename) throws QiniuException {
        try {
            manager.delete(config.getBucket(), filename);
        } catch (QiniuException e) {
            if (!(e.response != null && e.response.statusCode == 612)) {
                throw e;
            }
        }
    }


    @Getter
    @Setter
    private static class QiniuKodoClient {
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

    @Override
    protected String platform() {
        return StorageType.kodo.name();
    }

}
