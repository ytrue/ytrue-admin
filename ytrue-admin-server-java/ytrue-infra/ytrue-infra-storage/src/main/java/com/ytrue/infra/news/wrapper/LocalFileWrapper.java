package com.ytrue.infra.news.wrapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Getter
@Setter
@NoArgsConstructor
public class LocalFileWrapper implements FileWrapper {
    private File file;
    private String name;
    private String contentType;
    private InputStream inputStream;
    private Long size;

    public LocalFileWrapper(File file, String name, String contentType, Long size) throws IOException {
        this.file = file;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.inputStream;
    }
}
