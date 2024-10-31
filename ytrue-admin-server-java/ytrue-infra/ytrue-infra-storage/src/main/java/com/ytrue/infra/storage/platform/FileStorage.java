package com.ytrue.infra.storage.platform;

import com.qiniu.common.QiniuException;
import com.ytrue.infra.storage.entity.FileMetadata;
import com.ytrue.infra.storage.entity.UploadFileContext;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 文件存储接口，定义了文件上传、查询和删除的基本操作。
 * 实现该接口的类需要提供具体的文件存储逻辑。
 *
 * @author ytrue
 * @date 2024/10/29
 */
public interface FileStorage {

    /**
     * 上传文件。
     *
     * @param per 上传处理对象，包含文件的相关信息和操作。
     * @return 返回上传后生成的文件信息，包括文件的访问地址、大小等。
     * @throws Exception 如果上传过程中发生错误，抛出异常。
     */
    FileMetadata save(UploadFileContext per) throws Exception;

    /**
     * 判断文件是否存在。
     *
     * @param fileMetadata 文件信息对象，包含需要检查的文件的相关信息。
     * @return 如果文件存在，返回 true；否则返回 false。
     */
    boolean exists(FileMetadata fileMetadata) throws QiniuException;

    /**
     * 删除文件。
     *
     * @param fileMetadata 文件信息对象，包含要删除的文件的相关信息。
     * @return 如果删除成功，返回 true；否则返回 false。
     */
    boolean delete(FileMetadata fileMetadata);

    /**
     * 下载文件。
     *
     * @param fileMetadata 文件信息对象，包含要下载的文件的相关信息。
     * @param consumer 处理输入流的消费函数，通常用于将输入流写入目标输出流（如文件或响应）。
     * @throws Exception 如果下载过程中发生错误，抛出异常。
     */
    void download(FileMetadata fileMetadata, Consumer<InputStream> consumer) throws Exception;
}
