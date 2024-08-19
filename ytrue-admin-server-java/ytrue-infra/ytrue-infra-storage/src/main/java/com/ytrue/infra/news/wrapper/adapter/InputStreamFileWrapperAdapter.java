package com.ytrue.infra.news.wrapper.adapter;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.news.detect.ContentTypeDetect;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.news.wrapper.InputStreamFileWrapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


@AllArgsConstructor
public class InputStreamFileWrapperAdapter implements FileWrapperAdapter {

    private ContentTypeDetect contentTypeDetect;

    @Override
    public boolean isSupport(Object source) {
        return source instanceof InputStream || source instanceof InputStreamFileWrapper;
    }

    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException {

        // 如果是 InputStreamFileWrapper 直接算更新
        if (source instanceof InputStreamFileWrapper inputStreamFileWrapper) {
            return updateFileWrapper(inputStreamFileWrapper, name, contentType, size);
        }

        if (source instanceof InputStream inputStream) {
            // 文件名称
            name = StrUtil.isEmpty(name) ? "" : name;
            // 文件类型
            contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(inputStream, name) : contentType;
            // 文件大小
            size = Objects.isNull(size) ? inputStream.available() : size;
            return new InputStreamFileWrapper(inputStream, name, contentType, size);
        }

        throw new UnsupportedOperationException();

    }
}
