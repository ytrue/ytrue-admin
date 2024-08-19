package com.ytrue.infra.news.wrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件包装接口
 */
public interface FileWrapper {

    /**
     * 获取文件名称
     */
    String getName();

    /**
     * 设置文件名称
     */
    void setName(String name);

    /**
     * 获取文件的 MIME 类型
     */
    String getContentType();

    /**
     * 设置文件的 MIME 类型
     */
    void setContentType(String contentType);

    /**
     * 获取文件的 InputStream
     */
    InputStream getInputStream() throws IOException;

    /**
     * 获取文件大小
     */
    Long getSize();

    /**
     * 设置文件大小
     */
    void setSize(Long size);
}
