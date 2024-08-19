package com.ytrue.infra.news.wrapper.adapter;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.news.detect.ContentTypeDetect;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.news.wrapper.LocalFileWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


@AllArgsConstructor
public class LocalFileWrapperAdapter implements FileWrapperAdapter {

    private ContentTypeDetect contentTypeDetect;

    @Override
    public boolean isSupport(Object source) {
        return source instanceof File || source instanceof LocalFileWrapper;
    }

    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException {
        // 如果是 LocalFileWrapper 直接算更新
        if (source instanceof LocalFileWrapper fileWrapper) {
            return updateFileWrapper(fileWrapper, name, contentType, size);
        }

        if (source instanceof File file) {
            // 文件名称
            name = StrUtil.isEmpty(name) ? file.getName() : name;
            // 文件名称
            contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(file) : contentType;
            // 文件大小
            size = Objects.isNull(size) ? file.length() : size;

            return new LocalFileWrapper(file, name, contentType, size);
        }

        throw new UnsupportedOperationException();
    }
}
