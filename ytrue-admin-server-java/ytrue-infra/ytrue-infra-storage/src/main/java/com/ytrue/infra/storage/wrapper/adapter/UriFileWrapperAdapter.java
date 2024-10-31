package com.ytrue.infra.storage.wrapper.adapter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.ytrue.infra.storage.detect.ContentTypeDetect;
import com.ytrue.infra.storage.detect.TikaContentTypeDetect;
import com.ytrue.infra.storage.wrapper.FileWrapper;
import com.ytrue.infra.storage.wrapper.UriFileWrapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * UriFileWrapperAdapter 类实现了 FileWrapperAdapter 接口，
 * 用于适配 URI、URL 和 UriFileWrapper 的文件包装。
 *
 * <p>该适配器负责判断支持的文件源类型并返回相应的文件包装实例。</p>
 */
@AllArgsConstructor
public class UriFileWrapperAdapter implements FileWrapperAdapter {

    /**
     * 内容类型检测器，用于检测文件的 MIME 类型。
     */
    private ContentTypeDetect contentTypeDetect;

    /**
     * 默认构造方法，初始化 UriFileWrapperAdapter 实例。
     * <p>
     * 如果未提供内容类型检测器，则使用默认的 TikaContentTypeDetect 实例进行初始化。
     * </p>
     */
    public UriFileWrapperAdapter() {
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
        if (source instanceof UriFileWrapper) {
            return true; // 支持 UriFileWrapper 类型
        }
        if (source instanceof URL) {
            return true; // 支持 URL 类型
        }
        if (source instanceof URI) {
            return true; // 支持 URI 类型
        }
        if (source instanceof String) {
            try {
                // 尝试将字符串转换为 URL，支持 URL 字符串
                URLUtil.url((String) source);
                return true;
            } catch (Exception ignored) {
                // 转换失败，继续检查其他类型
            }
        }
        return false; // 不支持其他类型
    }

    /**
     * 获取对应于给定源的文件包装。
     *
     * @param source      文件源，可以是 UriFileWrapper、URL、URI 或字符串
     * @param name        文件名称
     * @param contentType 文件的 MIME 类型
     * @param size        文件大小，以字节为单位
     * @return 对应的 FileWrapper 实例
     * @throws IOException 如果在读取文件时发生 I/O 错误
     * @throws UnsupportedOperationException 如果源类型不受支持
     */
    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException {

        // 如果源是 UriFileWrapper，直接更新并返回
        if (source instanceof UriFileWrapper uriFileWrapper) {
            return updateFileWrapper(uriFileWrapper, name, contentType, size);
        }

        // 将 URI、字符串或 URL 转换为 URL 实例
        URL url;
        if (source instanceof URI uri) {
            url = uri.toURL(); // URI 转换为 URL
        } else if (source instanceof String urlString) {
            url = URLUtil.url(urlString); // 字符串转换为 URL
        } else if (source instanceof URL url1) {
            url = url1; // 已是 URL 类型
        } else {
            throw new UnsupportedOperationException("Unsupported source type: " + source.getClass());
        }

        // 打开 URL 连接并获取输入流
        URLConnection conn = url.openConnection();
        InputStream inputStream = IoUtil.toMarkSupportStream(conn.getInputStream());

        // 文件名称
        if (name == null) name = getName(conn, url);
        name = StrUtil.isEmpty(name) ? getName(conn, url) : name; // 若未提供则从连接获取名称

        // 文件大小
        if (Objects.isNull(size)) {
            size = conn.getContentLengthLong() < 0 ? null : conn.getContentLengthLong();
        }

        // 文件类型
        contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(inputStream, name) : contentType;

        return new UriFileWrapper(inputStream, name, contentType, size); // 返回 UriFileWrapper 实例
    }

    /**
     * 从 URLConnection 中提取文件名称。
     *
     * @param conn URLConnection 对象
     * @param url  URL 对象
     * @return 提取的文件名称
     */
    public String getName(URLConnection conn, URL url) {
        String name = "";
        String disposition = conn.getHeaderField("Content-Disposition"); // 获取 Content-Disposition 头

        // 如果有 Content-Disposition，尝试从中提取文件名称
        if (StrUtil.isNotBlank(disposition)) {
            name = ReUtil.get("filename=\"(.*?)\"", disposition, 1); // 正则提取
            if (StrUtil.isBlank(name)) {
                name = StrUtil.subAfter(disposition, "filename=", true); // 处理无引号的情况
            }
        }

        // 如果仍未找到名称，则从 URL 的路径中提取
        if (StrUtil.isBlank(name)) {
            final String path = url.getPath();
            name = StrUtil.subSuf(path, path.lastIndexOf('/') + 1); // 获取路径最后部分
            if (StrUtil.isNotBlank(name)) {
                name = URLUtil.decode(name, StandardCharsets.UTF_8); // 解码文件名
            }
        }
        return name; // 返回提取的名称
    }

}
