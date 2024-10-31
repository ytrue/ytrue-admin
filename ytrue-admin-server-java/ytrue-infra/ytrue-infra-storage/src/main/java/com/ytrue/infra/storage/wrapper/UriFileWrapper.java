package com.ytrue.infra.storage.wrapper;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

/**
 * UriFileWrapper 类实现了 FileWrapper 接口，封装了文件的基本信息和输入流。
 *
 * <p>该类用于处理来自 URI 的文件内容，提供文件的名称、内容类型、输入流和大小信息。</p>
 *
 * @author ytrue
 * @date 2024/10/30
 */
@Getter
@Setter
@NoArgsConstructor
public class UriFileWrapper implements FileWrapper {
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
     * <p>使用 Hutool 的 IoUtil 进行流的包装，以支持标记。</p>
     */
    private InputStream inputStream;

    /**
     * 文件大小，以字节为单位。
     */
    private Long size;

    /**
     * 构造函数，用于创建 UriFileWrapper 实例。
     *
     * @param inputStream 文件内容的输入流
     * @param name        文件名
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     */
    public UriFileWrapper(InputStream inputStream, String name, String contentType, Long size) {
        this.name = name;
        this.contentType = contentType;
        this.inputStream = IoUtil.toMarkSupportStream(inputStream); // 转换为支持标记的输入流
        this.size = size;
    }

    /**
     * 获取文件内容的输入流。
     *
     * @return 输入流
     */
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
