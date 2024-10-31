package com.ytrue.infra.storage.wrapper.adapter;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.storage.detect.ContentTypeDetect;
import com.ytrue.infra.storage.detect.TikaContentTypeDetect;
import com.ytrue.infra.storage.wrapper.ByteFileWrapper;
import com.ytrue.infra.storage.wrapper.FileWrapper;
import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * ByteFileWrapperAdapter 类实现了 FileWrapperAdapter 接口，用于适配字节数组和 ByteFileWrapper 的文件包装。
 *
 * <p>该适配器负责判断支持的文件源类型并返回相应的文件包装实例。</p>
 */
@AllArgsConstructor
public class ByteFileWrapperAdapter implements FileWrapperAdapter {

    /**
     * 内容类型检测器，用于检测文件的 MIME 类型。
     */
    private ContentTypeDetect contentTypeDetect;

    /**
     * 默认构造方法，初始化 ByteFileWrapperAdapter 实例。
     * <p>
     * 如果未提供内容类型检测器，则使用默认的 TikaContentTypeDetect 实例进行初始化。
     * </p>
     */
    public ByteFileWrapperAdapter() {
        if (contentTypeDetect == null) {
            contentTypeDetect = new TikaContentTypeDetect();
        }
    }

    /**
     * 判断是否支持给定的文件源。
     *
     * @param source 待检测的文件源
     * @return 如果支持，则返回 true；否则返回 false
     */
    @Override
    public boolean isSupport(Object source) {
        // 判断 source 是否为 byte[] 或 ByteFileWrapper 的实例
        return source instanceof byte[] || source instanceof ByteFileWrapper;
    }

    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source      文件源，可以是 byte[] 或 ByteFileWrapper
     * @param name        文件名称
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     * @return 对应的 FileWrapper 实例
     * @throws UnsupportedOperationException 如果源类型不受支持
     */
    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) {
        // 如果源是 ByteFileWrapper，直接更新并返回
        if (source instanceof ByteFileWrapper byteFileWrapper) {
            return updateFileWrapper(byteFileWrapper, name, contentType, size);
        }

        if (source instanceof byte[] bytes) {
            // 文件名称，若为空则设置为默认空字符串
            name = StrUtil.isEmpty(name) ? "" : name;
            // 检测文件的 MIME 类型，如果未提供，则使用检测器进行检测
            contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(bytes, name) : contentType;
            // 获取文件大小，若未提供，则使用字节数组的长度
            size = Objects.isNull(size) ? (long) bytes.length : size;
            // 创建并返回新的 ByteFileWrapper 实例
            return new ByteFileWrapper(bytes, name, contentType, size);
        }

        // 如果源类型不支持，抛出异常
        throw new UnsupportedOperationException("Unsupported source type: " + source.getClass());
    }
}
