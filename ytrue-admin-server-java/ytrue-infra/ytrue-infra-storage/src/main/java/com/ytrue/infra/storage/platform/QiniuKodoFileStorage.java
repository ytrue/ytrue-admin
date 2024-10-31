package com.ytrue.infra.storage.platform;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ytrue.infra.storage.entity.FileMetadata;
import com.ytrue.infra.storage.entity.UploadFileContext;
import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
import com.ytrue.infra.storage.exception.FileStorageException;
import com.ytrue.infra.storage.factory.QiniuKodoFileStorageClientFactory;
import com.ytrue.infra.storage.properties.QiniuKodoStorageProperties;
import com.ytrue.infra.storage.util.StringJoinerUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 七牛云 Kodo 文件存储实现。
 * 该类提供文件的上传、检查存在性和删除操作。
 *
 * @author ytrue
 * @date 2024/10/29
 */
@Getter
@Slf4j
public class QiniuKodoFileStorage extends AbstractFileStorage<QiniuKodoStorageProperties> {

    /**
     * 七牛云 Kodo 客户端工厂，用于创建和管理七牛云 Kodo 客户端实例。
     */
    private final QiniuKodoFileStorageClientFactory clientFactory;

    /**
     * 七牛云上传管理器，用于执行文件上传操作。
     */
    private final UploadManager uploadManager;

    /**
     * 七牛云存储桶管理器，用于管理存储桶中的文件（如删除、检查文件）。
     */
    private final BucketManager bucketManager;

    /**
     * 七牛云认证对象，用于生成上传凭证。
     */
    private final Auth auth;



    /**
     * 构造函数，初始化七牛云 Kodo 文件存储实例。
     *
     * @param properties 七牛云 Kodo 存储配置属性，用于创建客户端工厂和其他相关配置。
     */
    public QiniuKodoFileStorage(QiniuKodoStorageProperties properties) {
        // 调用父类构造函数，将配置属性传递给抽象文件存储类
        super(properties);

        // 保存客户端工厂，以便后续获取配置或客户端实例。
        this.clientFactory = new QiniuKodoFileStorageClientFactory(properties);

        // 通过工厂获取七牛云 Kodo 客户端
        QiniuKodoFileStorageClientFactory.QiniuKodoClient client = clientFactory.getClient();

        // 初始化上传管理器，用于执行文件上传操作
        this.uploadManager = client.getUploadManager();

        // 初始化存储桶管理器，用于文件管理操作（如删除、检查文件）
        this.bucketManager = client.getBucketManager();

        // 初始化认证对象，用于生成上传凭证
        this.auth = client.getAuth();
    }



    /**
     * 上传文件。
     *
     * @param context 上传文件上下文，包括文件的相关信息和处理逻辑
     * @return 返回上传后生成的文件元数据，包括文件的访问地址、大小、名称等
     * @throws Exception 如果上传过程中发生错误，抛出异常
     */
    @Override
    public FileMetadata save(UploadFileContext context) throws Exception {
        // 调用父类方法创建 FileMetadata，并传入平台标识、主机路径和域名信息
        FileMetadata fileMetadata = createFileMetadata(context, platform(), config.getFileHost(), config.getDomain());
        // 尝试上传文件并捕获可能的异常
        try (InputStream in = context.getFileWrapper().getInputStream()) {
            String token = auth.uploadToken(config.getBucket());
            // 上传
            uploadManager.put(in, StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()), token, null, fileMetadata.getContentType());
            // 返回文件元数据
            return fileMetadata;
        } catch (IOException e) {
            try {
                bucketManager.delete(config.getBucket(), StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()));
            } catch (QiniuException ignored) {
            }
            throw new FileStorageException("文件上传失败！platform：" + platform() + "，filename：" + fileMetadata.getOriginalFilename(), e);
        }

    }

    /**
     * 判断文件是否存在。
     *
     * @param fileMetadata 文件元数据对象，包含需要检查的文件的相关信息
     * @return 如果文件存在，返回 true；否则返回 false
     */
    @Override
    public boolean exists(FileMetadata fileMetadata) throws QiniuException {
        try {
            return bucketManager.stat(config.getBucket(), StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename())) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件。
     *
     * @param fileMetadata 文件元数据对象，包含要删除的文件的相关信息
     * @return 如果删除成功，返回 true；否则返回 false
     */
    @Override
    public boolean delete(FileMetadata fileMetadata) {
        try {
            bucketManager.delete(config.getBucket(), StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()));
            return true;
        } catch (Exception e) {
            log.error("删除文件失败：文件名 = " + fileMetadata.getFilename() + "，错误信息 = " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 下载文件。
     *
     * @param fileMetadata 文件元数据对象，包含要下载的文件的相关信息
     * @param consumer     处理输入流的消费函数，用于处理下载的文件流
     * @throws Exception 如果下载过程中发生错误，抛出异常
     */
    @Override
    public void download(FileMetadata fileMetadata, Consumer<InputStream> consumer) throws Exception {
        String url = auth.privateDownloadUrl(fileMetadata.getUrl());
        try (InputStream in = new URL(url).openStream()) {
            consumer.accept(in);
        } catch (Exception e) {
            log.error("下载文件失败：文件名 = " + fileMetadata.getFilename() + "，错误信息 = " + e.getMessage(), e);
            throw e;  // 抛出异常以便上层处理
        }
    }

    /**
     * 获取存储平台的名称。
     *
     * @return 返回存储平台的名称
     */
    @Override
    public String platform() {
        return FileStoragePlatformEnum.KODO.name();  // 返回存储平台枚举
    }
}
