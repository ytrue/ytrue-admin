package com.ytrue.infra.storage.wrapper.adapter;

import com.ytrue.infra.storage.wrapper.FileWrapper;

import java.io.IOException;

/**
 * 文件包装适配器接口，用于支持不同类型文件源的包装。
 *
 * <p>该接口定义了检测支持的文件源、获取文件包装和更新文件包装参数的方法。</p>
 */
public interface FileWrapperAdapter {

    /**
     * 判断是否支持给定的文件源。
     *
     * @param source 待检测的文件源
     * @return 如果支持，则返回 true；否则返回 false
     */
    boolean isSupport(Object source);

    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source 文件源
     * @return 对应的 FileWrapper 实例
     * @throws IOException 如果在获取文件包装时发生 I/O 错误
     */
    default FileWrapper getFileWrapper(Object source) throws IOException {
        return getFileWrapper(source, null, null, null);
    }


    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source 文件源
     * @param name   文件名称
     * @return 对应的 FileWrapper 实例
     * @throws IOException 如果在获取文件包装时发生 I/O 错误
     */
    default FileWrapper getFileWrapper(Object source, String name) throws IOException {
        return getFileWrapper(source, name, null, null);
    }

    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source      文件源
     * @param name        文件名称
     * @param contentType 文件的 MIME 类型
     * @return 对应的 FileWrapper 实例
     * @throws IOException 如果在获取文件包装时发生 I/O 错误
     */
    default FileWrapper getFileWrapper(Object source, String name, String contentType) throws IOException {
        return getFileWrapper(source, name, contentType, null);
    }

    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source      文件源
     * @param name        文件名称
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     * @return 对应的 FileWrapper 实例
     * @throws IOException 如果在获取文件包装时发生 I/O 错误
     */
    FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException;

    /**
     * 更新文件包装的参数。
     *
     * <p>如果参数不为 null，将更新对应的文件包装属性。</p>
     *
     * @param fileWrapper 需要更新的文件包装实例
     * @param name        新的文件名称
     * @param contentType 新的文件的 MIME 类型
     * @param size        新的文件大小，以字节为单位
     * @return 更新后的文件包装实例
     */
    default FileWrapper updateFileWrapper(FileWrapper fileWrapper, String name, String contentType, Long size) {
        if (name != null) {
            fileWrapper.setName(name);
        }
        if (contentType != null) {
            fileWrapper.setContentType(contentType);
        }
        if (size != null) {
            fileWrapper.setSize(size);
        }
        return fileWrapper;
    }
}
