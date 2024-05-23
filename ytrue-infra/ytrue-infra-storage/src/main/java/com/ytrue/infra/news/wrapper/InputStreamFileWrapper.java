package com.ytrue.infra.news.wrapper;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@NoArgsConstructor
public class InputStreamFileWrapper implements FileWrapper {

    private String name;
    private String contentType;
    private InputStream inputStream;
    private Long size;

    public InputStreamFileWrapper(InputStream inputStream, String name, String contentType, Long size) {
        this.name = name;
        this.contentType = contentType;
        // InputStream转换为支持mark标记的流,若原流支持mark标记，则返回原流，否则使用BufferedInputStream 包装
        this.inputStream = IoUtil.toMarkSupportStream(inputStream);
        this.size = size;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
