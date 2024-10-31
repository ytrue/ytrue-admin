package com.ytrue.infra.storage.builder;

import com.ytrue.infra.storage.wrapper.FileWrapper;
import com.ytrue.infra.storage.wrapper.adapter.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * 文件包装器构建器，用于方便地创建 FileWrapper 实例。
 *
 * <p>此类通过链式调用设置所需的适配器、源、文件名称、内容类型和大小等属性，
 * 最终生成对应的 FileWrapper 实例。</p>
 */
public class FileWrapperBuilder {

    private FileWrapperAdapter adapter;  // 文件包装适配器
    private Object source;                // 文件源（可以是不同类型的文件）
    private String name;                  // 文件名称
    private String contentType;           // 文件的 MIME 类型
    private Long size;                    // 文件大小（以字节为单位）

    /**
     * 适配器类型
     */
    private final FileWrapperAdapter[] defaultAdapters = {
            new LocalFileWrapperAdapter(),
            new ByteFileWrapperAdapter(),
            new InputStreamFileWrapperAdapter(),
            new UriFileWrapperAdapter()
    };

    /**
     * 设置文件包装适配器。
     *
     * @param adapter 文件包装适配器实例
     * @return 当前 FileWrapperBuilder 实例
     */
    public FileWrapperBuilder withAdapter(FileWrapperAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    /**
     * 设置文件源。
     *
     * @param source 文件源（可以是文件路径、字节数组等）
     * @return 当前 FileWrapperBuilder 实例
     */
    public FileWrapperBuilder withSource(Object source) {
        this.source = source;
        return this;
    }

    /**
     * 设置文件名称。
     *
     * @param name 文件名称
     * @return 当前 FileWrapperBuilder 实例
     */
    public FileWrapperBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 设置文件的 MIME 类型。
     *
     * @param contentType 文件的 MIME 类型
     * @return 当前 FileWrapperBuilder 实例
     */
    public FileWrapperBuilder withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * 设置文件大小（以字节为单位）。
     *
     * @param size 文件大小
     * @return 当前 FileWrapperBuilder 实例
     */
    public FileWrapperBuilder withSize(Long size) {
        this.size = size;
        return this;
    }

    /**
     * 创建 FileWrapper 实例。
     *
     * @return 构建的 FileWrapper 实例
     * @throws IOException                   如果在获取 FileWrapper 时发生 I/O 错误
     * @throws IllegalArgumentException      如果适配器未设置且没有找到支持的适配器
     * @throws UnsupportedOperationException 如果源类型不受支持
     */
    public FileWrapper build() throws IOException {
        // 如果 source 为 null，抛出异常
        if (source == null) {
            throw new IllegalArgumentException("source 不能为 null");
        }
        // 如果适配器未设置，尝试找到支持的适配器
        if (adapter == null) {
            // 使用 Stream API 查找第一个支持的适配器
            adapter = Arrays.stream(defaultAdapters)
                    // 筛选出支持的适配器
                    .filter(defaultAdapter -> defaultAdapter.isSupport(source))
                    // 获取第一个匹配的适配器
                    .findFirst()
                    // 如果没有找到，抛出异常
                    .orElseThrow(() -> new IllegalArgumentException("没有找到支持的 FileWrapperAdapter"));
        }
        // 使用找到的适配器创建并返回 FileWrapper 实例
        return adapter.getFileWrapper(source, name, contentType, size);
    }

}
