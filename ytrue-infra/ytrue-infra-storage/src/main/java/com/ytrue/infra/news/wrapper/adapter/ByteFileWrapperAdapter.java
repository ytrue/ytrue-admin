package com.ytrue.infra.news.wrapper.adapter;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.news.detect.ContentTypeDetect;
import com.ytrue.infra.news.wrapper.ByteFileWrapper;
import com.ytrue.infra.news.wrapper.FileWrapper;
import lombok.AllArgsConstructor;

import java.util.Objects;


@AllArgsConstructor
public class ByteFileWrapperAdapter implements FileWrapperAdapter {

    private ContentTypeDetect contentTypeDetect;

    @Override
    public boolean isSupport(Object source) {
        // 判断是不是 byte[] 或者是 ByteFileWrapper
        return source instanceof byte[] || source instanceof ByteFileWrapper;
    }

    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) {
        // 如果是 ByteFileWrapper 直接算更新
        if (source instanceof ByteFileWrapper byteFileWrapper) {
            return updateFileWrapper(byteFileWrapper, name, contentType, size);
        }

        if (source instanceof byte[] bytes) {
            // 文件名称
            name = StrUtil.isEmpty(name) ? "" : name;
            // 文件名称
            contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(bytes, name) : contentType;
            // 文件大小
            size = Objects.isNull(size) ? bytes.length : size;
            return new ByteFileWrapper(bytes, name, contentType, size);
        }

        throw new UnsupportedOperationException();
    }
}
