package com.ytrue.infra.storage.wrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件包装接口，用于定义文件相关操作的基本方法。
 *
 * <p>该接口提供了获取和设置文件名称、MIME 类型、输入流和大小的方法。</p>
 */
public interface FileWrapper {

    /**
     * 获取文件名称。
     *
     * @return 文件名称
     */
    String getName();

    /**
     * 设置文件名称。
     *
     * @param name 文件名称
     */
    void setName(String name);

    /**
     * 获取文件的 MIME 类型。
     *
     * @return 文件的 MIME 类型
     */
    String getContentType();

    /**
     * 设置文件的 MIME 类型。
     *
     * @param contentType 文件的 MIME 类型
     */
    void setContentType(String contentType);

    /**
     * 获取文件的输入流。
     *
     * @return 文件内容的输入流
     * @throws IOException 如果获取输入流时发生 I/O 错误
     */
    InputStream getInputStream() throws IOException;

    /**
     * 获取文件大小，以字节为单位。
     *
     * @return 文件大小
     */
    Long getSize();

    /**
     * 设置文件大小。
     *
     * @param size 文件大小，以字节为单位
     */
    void setSize(Long size);
}
