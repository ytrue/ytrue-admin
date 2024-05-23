package com.ytrue.infra.news.wrapper.adapter;

import com.ytrue.infra.news.wrapper.FileWrapper;

import java.io.IOException;

public interface FileWrapperAdapter {
    /**
     * 是否支持
     * @param source
     * @return
     */
    boolean isSupport(Object source);

    /**
     * 获取文件包装
     * @param source
     * @param name
     * @param contentType
     * @param size
     * @return
     * @throws IOException
     */
    FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException;


    /**
     * 更新文件包装参数
     * @param fileWrapper
     * @param name
     * @param contentType
     * @param size
     * @return
     */
    default FileWrapper updateFileWrapper(FileWrapper fileWrapper, String name, String contentType, Long size) {
        if (name != null) fileWrapper.setName(name);
        if (contentType != null) fileWrapper.setContentType(contentType);
        if (size != null) fileWrapper.setSize(size);
        return fileWrapper;
    }
}
