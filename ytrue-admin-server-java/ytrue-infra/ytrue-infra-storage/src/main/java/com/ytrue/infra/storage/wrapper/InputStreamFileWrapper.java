package com.ytrue.infra.storage.wrapper;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

/**
 * InputStreamFileWrapper 类实现了 FileWrapper 接口，封装了基于输入流的文件信息。
 *
 * <p>该类用于处理通过 InputStream 提供的文件内容，提供文件的名称、内容类型、输入流和大小信息。</p>
 *
 * @author ytrue
 * @date 2024/10/30
 */
@Getter
@Setter
@NoArgsConstructor
public class InputStreamFileWrapper implements FileWrapper {

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
     * <p>使用 Hutool 的 IoUtil 转换为支持 mark 标记的流。</p>
     */
    private InputStream inputStream;

    /**
     * 文件大小，以字节为单位。
     */
    private Long size;

    /**
     * 构造函数，用于创建 InputStreamFileWrapper 实例。
     *
     * @param inputStream 文件内容的输入流
     * @param name        文件名
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     */
    public InputStreamFileWrapper(InputStream inputStream, String name, String contentType, Long size) {
        this.name = name;
        this.contentType = contentType;
        // 将 InputStream 转换为支持 mark 标记的流，若原流已支持，则返回原流
        this.inputStream = IoUtil.toMarkSupportStream(inputStream);
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
