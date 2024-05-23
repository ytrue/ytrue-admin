package com.ytrue.infra.news.wrapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Getter
@Setter
@NoArgsConstructor
public class ByteFileWrapper implements FileWrapper {
    private byte[] bytes;
    private String name;
    private String contentType;
    private InputStream inputStream;
    private Long size;

    public ByteFileWrapper(byte[] bytes, String name, String contentType, Long size) {
        this.bytes = bytes;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.inputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }
}
