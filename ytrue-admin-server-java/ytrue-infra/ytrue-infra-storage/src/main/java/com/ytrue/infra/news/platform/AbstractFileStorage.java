package com.ytrue.infra.news.platform;

import com.ytrue.infra.news.FileMetadata;
import com.ytrue.infra.news.UploadFileContext;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 抽象文件存储类，提供了文件存储的基本实现。
 * 该类实现了 FileStorage 接口，提供了上传、检查和删除文件的基本方法，
 * 但具体实现需要由子类提供。
 *
 * @author ytrue
 * @date 2024/10/29
 */
public abstract class AbstractFileStorage implements FileStorage {

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
    public boolean exists(FileMetadata fileMetadata) {
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
     * @param consumer 处理输入流的消费函数，通常用于将输入流写入目标输出流（如文件或响应）。
     * @throws Exception 如果下载过程中发生错误，抛出异常。
     */
    @Override
    public void download(FileMetadata fileMetadata, Consumer<InputStream> consumer) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取存储平台的信息。
     * 子类需实现此方法以返回具体的平台名称或类型。
     *
     * @return 存储平台的名称或类型。
     */
    public abstract String platform();
}
