package com.ytrue.infra.news.platform;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.ytrue.infra.news.FileMetadata;
import com.ytrue.infra.news.UploadFileContext;
import com.ytrue.infra.news.enums.FileStoragePlatformEnum;
import com.ytrue.infra.news.factory.AliyunOssFileStorageClientFactory;
import com.ytrue.infra.news.util.MimeTypeUtil;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.storage.utils.StringJoinerUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * 阿里云 OSS 文件存储实现。
 * 该类提供文件的上传、检查存在性和删除操作。
 * <p>
 * 通过与阿里云 OSS 的接口交互，实现对文件的增删查改功能。
 *
 * @author ytrue
 * @date 2024/10/29
 */
@Slf4j
public class AliyunOssFileStorage extends AbstractFileStorage {

    /**
     * 客户端工厂，用于创建 OSS 客户端
     */
    private final AliyunOssFileStorageClientFactory clientFactory;

    /**
     *  阿里云 OSS 客户端实例
     */
    private final OSS ossClient;

    /**
     * 构造函数，初始化阿里云 OSS 客户端工厂。
     *
     * @param clientFactory 阿里云 OSS 文件存储客户端工厂
     *                     用于创建和管理 OSS 客户端实例
     */
    public AliyunOssFileStorage(AliyunOssFileStorageClientFactory clientFactory) {
        // 保存客户端工厂
        this.clientFactory = clientFactory;
        // 获取 OSS 客户端
        this.ossClient = clientFactory.getClient();
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
        // 获取文件包装器
        FileWrapper fileWrapper = context.getFileWrapper();
        // 创建 FileMetadata
        FileMetadata fileMetadata = new FileMetadata();
        // 设置时间
        fileMetadata.setCreateTime(LocalDateTime.now());
        // 设置文件大小
        fileMetadata.setSize(fileWrapper.getSize());
        // 文件的原生名
        String originalFileName = fileWrapper.getName();
        fileMetadata.setOriginalFilename(originalFileName);
        // 设置文件后缀
        String fileExtension = MimeTypeUtil.getPreferredFileExtension(originalFileName, fileWrapper.getContentType());
        fileMetadata.setExt(fileExtension);
        // contentType
        fileMetadata.setContentType(fileWrapper.getContentType());
        // 设置文件名称
        String newFileName = context.getSaveFileName();
        // 如果文件名称为空，这里随机生产名称
        if (StrUtil.isBlank(newFileName)) {
            newFileName = IdUtil.objectId() + (StrUtil.isBlank(fileMetadata.getExt()) ? StrUtil.EMPTY : fileMetadata.getExt());
        }
        fileMetadata.setFilename(newFileName);
        // 设置平台
        fileMetadata.setPlatform(platform());
        // url  basePath path
        String fileHost = clientFactory.getConfig().getFileHost();
        fileMetadata.setBasePath(fileHost);
        fileMetadata.setPath(StringJoinerUtil.joinWithSlash(fileHost, context.getSavePath()));
        // 设置url
        fileMetadata.setUrl(StringJoinerUtil.joinWithSlash(
                this.clientFactory.getConfig().getDomain(),
                fileMetadata.getPath(),
                fileMetadata.getFilename()
        ));


        // 设置文件元数据
        ObjectMetadata metadata = new ObjectMetadata();
        // 设置文件大小
        metadata.setContentLength(context.getFileWrapper().getSize());
        // 设置文件类型
        metadata.setContentType(context.getFileWrapper().getContentType());
        // 创建上传请求
        PutObjectRequest putRequest = new PutObjectRequest(
                clientFactory.getConfig().getBucket(),
                StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()),
                context.getFileWrapper().getInputStream(),
                metadata
        );
        // 执行上传
        ossClient.putObject(putRequest);
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
        // 检查文件存在性
        return ossClient.doesObjectExist(clientFactory.getConfig().getBucket(), StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()));
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
            // 删除文件
            ossClient.deleteObject(clientFactory.getConfig().getBucket(), StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename()));
            return true;  // 返回删除成功
        } catch (Exception e) {
            // 记录异常日志
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
            // 获取文件输入流
            InputStream inputStream = ossClient.getObject(
                    new GetObjectRequest(
                            clientFactory.getConfig().getBucket(),
                            StringJoinerUtil.joinWithSlash(fileMetadata.getPath(), fileMetadata.getFilename())
                    )).getObjectContent();
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
        // 返回存储平台枚举
        return FileStoragePlatformEnum.OSS.name();
    }
}
