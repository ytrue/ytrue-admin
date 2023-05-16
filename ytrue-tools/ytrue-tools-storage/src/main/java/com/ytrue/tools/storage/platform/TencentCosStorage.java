package com.ytrue.tools.storage.platform;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import com.ytrue.tools.storage.properties.TencentCosStorageProperties;
import com.ytrue.tools.storage.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description QcloudStorage
 */
public class TencentCosStorage extends AbstractStorage {

    private TencentCosStorageProperties config;

    private volatile COSClient client;

    public TencentCosStorage(TencentCosStorageProperties config) {
        this.config = config;
    }

    /**
     * 单例模式运行，不需要每次使用完再销毁了
     *
     * @return
     */
    public COSClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
                    ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
                    clientConfig.setHttpProtocol(HttpProtocol.https);
                    client = new COSClient(cred, clientConfig);
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

        COSClient client = getClient();
        try (InputStream in = uploadInfo.getFileWrapper().getInputStream()) {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileInfo.getSize());
            metadata.setContentType(fileInfo.getContentType());
            client.putObject(config.getBucket(), newFileKey, in, metadata);

            return fileInfo;
        } catch (IOException e) {
            client.deleteObject(config.getBucket(), newFileKey);
            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileInfo.getOriginalFilename(), e);
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        COSClient client = getClient();
        client.deleteObject(config.getBucket(), PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath(), fileInfo.getFileName()));
        return true;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return getClient().doesObjectExist(config.getBucket(), PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath(), fileInfo.getFileName()));
    }

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        COSObject object = getClient().getObject(config.getBucket(), PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath(), fileInfo.getFileName()));
        try (InputStream in = object.getObjectContent()) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new StorageRuntimeException("文件下载失败！platform：" + fileInfo, e);
        }
    }

    @Override
    public String platform() {
        return StorageType.cos.name();
    }
}
