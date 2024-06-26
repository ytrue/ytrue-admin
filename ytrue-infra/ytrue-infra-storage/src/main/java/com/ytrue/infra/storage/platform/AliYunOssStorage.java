package com.ytrue.infra.storage.platform;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.ytrue.infra.storage.enums.StorageType;
import com.ytrue.infra.storage.exception.StorageRuntimeException;
import com.ytrue.infra.storage.model.FileInfo;
import com.ytrue.infra.storage.model.UploadInfo;
import com.ytrue.infra.storage.properties.AliyunOssStorageProperties;
import com.ytrue.infra.storage.utils.PathUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description AliYunOssStorage
 */
@Getter
@Setter
public class AliYunOssStorage extends AbstractStorage {


    private final AliyunOssStorageProperties config;

    private volatile OSS client;

    public AliYunOssStorage(AliyunOssStorageProperties config) {
        this.config = config;
    }


    /**
     * 单例模式运行，不需要每次使用完再销毁了,这个在spring模式下面才能提现作用
     *
     * @return
     */
    public OSS getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
                }
            }
        }
        return client;
    }


    @Override
    public FileInfo upload(UploadInfo uploadInfo) {
        FileInfo fileInfo = buildFileInfo(uploadInfo);

        String newFileKey = PathUtil.montagePath(config.getFileHost(), fileInfo.getPath(), fileInfo.getFileName());

        fileInfo.setBasePath(config.getFileHost());
        fileInfo.setUrl(config.getDomain() + "/" + newFileKey);

        OSS client = getClient();
        try (InputStream in = uploadInfo.getFileWrapper().getInputStream()) {
            // 设置meta
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileInfo.getSize());
            metadata.setContentType(fileInfo.getContentType());

            // 上传
            client.putObject(config.getBucket(), newFileKey, in, metadata);

            return fileInfo;
        } catch (IOException e) {
            client.deleteObject(config.getBucket(), newFileKey);
            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileInfo.getOriginalFilename(), e);
        }
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return getClient().doesObjectExist(config.getBucket(), PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath(), fileInfo.getFileName()));
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        OSS client = getClient();
        client.deleteObject(config.getBucket(), PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath(), fileInfo.getFileName()));
        return true;
    }


    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        OSSObject object = getClient().getObject(config.getBucket(), PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath(), fileInfo.getFileName()));
        try (InputStream in = object.getObjectContent()) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new StorageRuntimeException("文件下载失败！platform：" + fileInfo, e);
        }
    }

    @Override
    public String platform() {
        return StorageType.oss.name();
    }
}
