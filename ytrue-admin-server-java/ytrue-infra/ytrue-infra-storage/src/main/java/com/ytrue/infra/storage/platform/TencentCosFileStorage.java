package com.ytrue.infra.storage.platform;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.ytrue.infra.storage.entity.FileMetadata;
import com.ytrue.infra.storage.entity.UploadFileContext;
import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
import com.ytrue.infra.storage.factory.TencentCosFileStorageClientFactory;
import com.ytrue.infra.storage.properties.TencentCosStorageProperties;
import com.ytrue.infra.storage.util.StringJoinerUtil;
import com.ytrue.infra.storage.wrapper.FileWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 腾讯云 COS 文件存储实现。
 * 该类提供文件的上传、检查存在性、删除和下载操作。
 * <p>
 * 通过与腾讯云 COS 的接口交互，实现对文件的增删查改功能。
 *
 * @author ytrue
 * @date 2024/10/29
 */
@Getter
@Slf4j
public class TencentCosFileStorage extends AbstractFileStorage<TencentCosStorageProperties> {

    /**
     * 客户端工厂，用于创建 COS 客户端。
     */
    private final TencentCosFileStorageClientFactory clientFactory;

    /**
     * 腾讯云 COS 客户端实例，用于与腾讯云 COS 进行文件交互。
     */
    private final COSClient cosClient;


    /**
     * 构造函数，初始化腾讯云 COS 文件存储实例。
     *
     * @param properties 腾讯云 COS 存储配置属性，用于创建客户端工厂和其他相关配置。
     */
    public TencentCosFileStorage(TencentCosStorageProperties properties) {
        // 调用父类构造函数，将配置属性传递给抽象文件存储类
        super(properties);

        // 保存客户端工厂以便后续获取客户端实例或配置信息
        this.clientFactory = new TencentCosFileStorageClientFactory(properties);

        // 使用客户端工厂获取 COS 客户端实例
        this.cosClient = clientFactory.getClient();
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
        FileWrapper fileWrapper = context.getFileWrapper();
        // 调用父类方法创建 FileMetadata，并传入平台标识、主机路径和域名信息
        FileMetadata fileMetadata = createFileMetadata(context, platform(), config.getFileHost(), config.getDomain());
        // 设置文件元数据
        ObjectMetadata metadata = new ObjectMetadata();
        // 设置文件大小
        metadata.setContentLength(fileWrapper.getSize());
        // 设置文件类型
        metadata.setContentType(fileWrapper.getContentType());

        // 创建上传请求
        PutObjectRequest putRequest = new PutObjectRequest(
                config.getBucket(),
                StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()),
                fileWrapper.getInputStream(),
                metadata
        );

        // 执行上传
        cosClient.putObject(putRequest);
        // 返回文件元数据
        return fileMetadata;
    }

    /**
     * 判断文件是否存在。
     *
     * @param fileMetadata 文件元数据对象，包含需要检查的文件的相关信息
     * @return 如果文件存在，返回 true；否则返回 false
     */
    @Override
    public boolean exists(FileMetadata fileMetadata) {
        try {
            COSObject cosObject = cosClient.getObject(
                    config.getBucket(),
                    StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename())
            );
            return cosObject != null && cosObject.getObjectContent() != null;
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
            cosClient.deleteObject(config.getBucket(),
                    StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()));
            return true;  // 返回删除成功
        } catch (Exception e) {
            log.error("删除文件失败：文件名 = " + fileMetadata.getFilename() + "，错误信息 = " + e.getMessage(), e);
            return false;  // 返回删除失败
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
        try {
            COSObject cosObject = cosClient.getObject(
                    config.getBucket(),
                    StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename())
            );
            InputStream inputStream = cosObject.getObjectContent();
            // 使用消费者处理输入流
            consumer.accept(inputStream);
        } catch (Exception e) {
            log.error("下载文件失败：文件名 = " + fileMetadata.getFilename() + "，错误信息 = " + e.getMessage(), e);
            throw e;  // 抛出异常以便上层处理
        }
    }

    /**
     * 获取存储平台的名称。
     *
     * @return 返回存储平台的名称，使用枚举表示
     */
    @Override
    public String platform() {
        return FileStoragePlatformEnum.COS.name();  // 返回存储平台枚举
    }
}
