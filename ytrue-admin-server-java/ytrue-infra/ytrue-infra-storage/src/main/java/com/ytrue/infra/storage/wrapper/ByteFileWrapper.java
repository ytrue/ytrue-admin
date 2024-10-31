package com.ytrue.infra.storage.wrapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * ByteFileWrapper 类实现了 FileWrapper 接口，封装了字节数组形式的文件信息。
 *
 * <p>该类用于处理通过字节数组提供的文件内容，提供文件的名称、内容类型、输入流和大小信息。</p>
 *
 * @author ytrue
 * @date 2024/10/30
 */
@Getter
@Setter
@NoArgsConstructor
public class ByteFileWrapper implements FileWrapper {

    /**
     * 文件的字节数组。
     */
    private byte[] bytes;

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
     * <p>通过字节数组创建 ByteArrayInputStream。</p>
     */
    private InputStream inputStream;

    /**
     * 文件大小，以字节为单位。
     */
    private Long size;

    /**
     * 构造函数，用于创建 ByteFileWrapper 实例。
     *
     * @param bytes       文件的字节数组
     * @param name        文件名
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     */
    public ByteFileWrapper(byte[] bytes, String name, String contentType, Long size) {
        this.bytes = bytes;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        // 将字节数组转换为输入流
        this.inputStream = new ByteArrayInputStream(bytes);
    }

    /**
     * 获取文件内容的输入流。
     *
     * @return 输入流
     */
    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }
}
