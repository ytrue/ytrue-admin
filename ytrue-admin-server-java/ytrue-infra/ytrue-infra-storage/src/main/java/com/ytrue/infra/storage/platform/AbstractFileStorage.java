package com.ytrue.infra.storage.platform;

import cn.hutool.core.util.IdUtil;
import com.qiniu.common.QiniuException;
import com.ytrue.infra.storage.entity.FileMetadata;
import com.ytrue.infra.storage.entity.UploadFileContext;
import com.ytrue.infra.storage.properties.BaseStorageProperties;
import com.ytrue.infra.storage.util.MimeTypeUtil;
import com.ytrue.infra.storage.util.StringJoinerUtil;
import com.ytrue.infra.storage.wrapper.FileWrapper;
import org.apache.tika.mime.MimeTypeException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * 抽象文件存储类，提供了文件存储的基本实现。
 * 该类实现了 FileStorage 接口，提供了上传、检查和删除文件的基本方法，
 * 但具体实现需要由子类提供。
 *
 * @param <C> 表示存储配置的类型，必须是 BaseStorageProperties 的子类
 * @author ytrue
 * @date 2024/10/29
 */
public abstract class AbstractFileStorage<C extends BaseStorageProperties> implements FileStorage {

    /**
     * 存储配置对象，包含文件存储相关的配置信息。
     */
    protected C config;


    @SuppressWarnings("unchecked")
    @Override
    public C getConfig() {
        return config; // 直接返回类型参数 config
    }

    /**
     * 构造函数，用于初始化存储配置。
     *
     * @param config 存储配置对象，必须是 BaseStorageProperties 的实例
     */
    public AbstractFileStorage(C config) {
        this.config = config;  // 将配置对象赋值给成员变量
    }


    /**
     * 上传文件。
     * 子类需实现此方法以提供具体的文件上传逻辑。
     *
     * @param uploadContext 上传文件上下文对象，包含文件的相关信息和操作。
     * @return 返回上传后生成的文件元数据，包括文件的访问地址、大小等。
     * @throws Exception 如果上传过程中发生错误，抛出异常。
     */
    @Override
    public FileMetadata save(UploadFileContext uploadContext) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断文件是否存在。
     * 子类需实现此方法以提供具体的文件存在性检查逻辑。
     *
     * @param fileMetadata 文件元数据对象，包含需要检查的文件的相关信息。
     * @return 如果文件存在，返回 true；否则返回 false。
     */
    @Override
    public boolean exists(FileMetadata fileMetadata) throws QiniuException {
        throw new UnsupportedOperationException();
    }

    /**
     * 删除文件。
     * 子类需实现此方法以提供具体的文件删除逻辑。
     *
     * @param fileMetadata 文件元数据对象，包含要删除的文件的相关信息。
     * @return 如果删除成功，返回 true；否则返回 false。
     */
    @Override
    public boolean delete(FileMetadata fileMetadata) {
        throw new UnsupportedOperationException();
    }


    /**
     * 下载文件。
     *
     * @param fileMetadata 文件信息对象，包含要下载的文件的相关信息。
     * @param consumer     处理输入流的消费函数，通常用于将输入流写入目标输出流（如文件或响应）。
     * @throws Exception 如果下载过程中发生错误，抛出异常。
     */
    @Override
    public void download(FileMetadata fileMetadata, Consumer<InputStream> consumer) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取存储平台的信息。
     *
     * @return 存储平台的名称或类型。
     */
    public String platform() {
        return config.getPlatform();
    }


    /**
     * 创建并初始化 FileMetadata 对象。
     * 该方法将从上传上下文 `UploadFileContext` 中提取文件信息，并设置元数据的各项属性。
     * 此方法减少了在每个子类中重复代码的编写，提升了代码复用性。
     *
     * @param context  上传文件上下文，包括文件的相关信息和处理逻辑
     * @param platform 文件存储平台的标识，用于区分不同的存储平台
     * @param fileHost 文件存储的主机路径，用于构建文件的基础路径
     * @param domain   文件存储的域名，用于构建文件的完整访问 URL
     * @return 初始化后的文件元数据对象 `FileMetadata`
     */
    protected FileMetadata createFileMetadata(UploadFileContext context, String platform, String fileHost, String domain) throws MimeTypeException {
        // 从上传上下文中获取文件包装器，包含文件的具体信息（如名称、类型、大小等）
        FileWrapper fileWrapper = context.getFileWrapper();

        // 创建 FileMetadata 对象，用于存储文件的元数据信息
        FileMetadata fileMetadata = new FileMetadata();

        // 设置文件的创建时间，表示文件上传的时间点
        fileMetadata.setCreateTime(LocalDateTime.now());

        // 设置文件大小，从文件包装器中获取文件的大小信息
        fileMetadata.setSize(fileWrapper.getSize());

        // 获取并设置文件的原始名称
        fileMetadata.setOriginalFilename(fileWrapper.getName());

        // 设置文件后缀，使用文件原始名称和内容类型推测出文件的扩展名
        String fileExtension = MimeTypeUtil.getPreferredFileExtension(fileWrapper.getName(), fileWrapper.getContentType());
        fileMetadata.setExt(fileExtension);

        // 设置文件的 MIME 类型
        fileMetadata.setContentType(fileWrapper.getContentType());

        // 获取自定义保存的文件名，如果为空则随机生成唯一名称
        String newFileName = context.getSaveFileName();
        if (newFileName == null || newFileName.isEmpty()) {
            // 使用唯一 ID 生成器生成新的文件名称，并附加扩展名（如果存在）
            newFileName = IdUtil.objectId() + (fileExtension != null ? fileExtension : "");
        }
        fileMetadata.setFilename(newFileName);

        // 设置文件存储平台标识，帮助标识此文件所属的存储平台
        fileMetadata.setPlatform(platform);

        // 设置文件的基础路径（主机路径），用于文件的本地存储路径
        fileMetadata.setBasePath(fileHost);

        // 拼接文件的路径，包括基础路径和保存路径，用于文件的存储位置
        fileMetadata.setPath(StringJoinerUtil.joinWithSlash(fileHost, context.getSavePath()));

        // 设置文件的完整访问 URL，包括域名、文件路径和文件名
        fileMetadata.setUrl(StringJoinerUtil.joinWithSlash(domain, fileMetadata.getPath(), newFileName));

        return fileMetadata;
    }
}
