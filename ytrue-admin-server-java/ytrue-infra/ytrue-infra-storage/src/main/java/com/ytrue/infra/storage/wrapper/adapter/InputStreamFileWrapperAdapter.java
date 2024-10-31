package com.ytrue.infra.storage.wrapper.adapter;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.storage.detect.ContentTypeDetect;
import com.ytrue.infra.storage.detect.TikaContentTypeDetect;
import com.ytrue.infra.storage.wrapper.FileWrapper;
import com.ytrue.infra.storage.wrapper.InputStreamFileWrapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * InputStreamFileWrapperAdapter 类实现了 FileWrapperAdapter 接口，
 * 用于适配 InputStream 和 InputStreamFileWrapper 的文件包装。
 *
 * <p>该适配器负责判断支持的文件源类型并返回相应的文件包装实例。</p>
 */
@AllArgsConstructor
public class InputStreamFileWrapperAdapter implements FileWrapperAdapter {

    /**
     * 内容类型检测器，用于检测文件的 MIME 类型。
     */
    private ContentTypeDetect contentTypeDetect;

    /**
     * 默认构造方法，初始化 InputStreamFileWrapperAdapter 实例。
     * <p>
     * 如果未提供内容类型检测器，则使用默认的 TikaContentTypeDetect 实例进行初始化。
     * </p>
     */
    public InputStreamFileWrapperAdapter() {
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
        // 判断 source 是否为 InputStream 或 InputStreamFileWrapper 的实例
        return source instanceof InputStream || source instanceof InputStreamFileWrapper;
    }

    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source      文件源，可以是 InputStream 或 InputStreamFileWrapper
     * @param name        文件名称
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     * @return 对应的 FileWrapper 实例
     * @throws IOException 如果在读取输入流时发生 I/O 错误
     * @throws UnsupportedOperationException 如果源类型不受支持
     */
    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException {
        // 如果源是 InputStreamFileWrapper，直接更新并返回
        if (source instanceof InputStreamFileWrapper inputStreamFileWrapper) {
            return updateFileWrapper(inputStreamFileWrapper, name, contentType, size);
        }

        if (source instanceof InputStream inputStream) {
            // 文件名称，若为空则设置为默认空字符串
            name = StrUtil.isEmpty(name) ? "" : name;
            // 检测文件的 MIME 类型，如果未提供，则使用检测器进行检测
            contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(inputStream, name) : contentType;
            // 获取文件大小，若未提供，则使用输入流的可用字节数
            size = Objects.isNull(size) ? (long) inputStream.available() : size;
            // 创建并返回新的 InputStreamFileWrapper 实例
            return new InputStreamFileWrapper(inputStream, name, contentType, size);
        }

        // 如果源类型不支持，抛出异常
        throw new UnsupportedOperationException("Unsupported source type: " + source.getClass());
    }
}
