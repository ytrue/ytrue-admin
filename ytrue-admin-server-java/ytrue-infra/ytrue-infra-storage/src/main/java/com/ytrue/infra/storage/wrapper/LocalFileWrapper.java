package com.ytrue.infra.storage.wrapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * LocalFileWrapper 类实现了 FileWrapper 接口，封装了本地文件的基本信息和输入流。
 *
 * <p>该类用于处理本地文件，提供文件的名称、内容类型、输入流和大小信息。</p>
 *
 * @author ytrue
 * @date 2024/10/30
 */
@Getter
@Setter
@NoArgsConstructor
public class LocalFileWrapper implements FileWrapper {
    /**
     * 代表本地文件的 File 对象。
     */
    private File file;

    /**
     * 文件名。
     */
    private String name;

    /**
     * 文件的 MIME 类型。
     */
    private String contentType;

    /**
     * 文件内容的输入流。
     * <p>使用 BufferedInputStream 包装以提高读取效率。</p>
     */
    private InputStream inputStream;

    /**
     * 文件大小，以字节为单位。
     */
    private Long size;

    /**
     * 构造函数，用于创建 LocalFileWrapper 实例。
     *
     * @param file        本地文件对象
     * @param name        文件名
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     * @throws IOException 如果读取文件时发生 I/O 错误
     */
    public LocalFileWrapper(File file, String name, String contentType, Long size) throws IOException {
        this.file = file;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        // 创建 BufferedInputStream，增强输入流的读取效率
        this.inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
    }

    /**
     * 获取文件内容的输入流。
     *
     * @return 输入流
     * @throws IOException 如果在获取输入流时发生 I/O 错误
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return this.inputStream;
    }
}
