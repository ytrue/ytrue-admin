package com.ytrue.infra.news.wrapper.adapter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.ytrue.infra.news.detect.ContentTypeDetect;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.news.wrapper.UriFileWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@AllArgsConstructor
public class UriFileWrapperAdapter implements FileWrapperAdapter {

    private ContentTypeDetect contentTypeDetect;

    @Override
    public boolean isSupport(Object source) {
        if (source instanceof UriFileWrapper) {
            return true;
        }
        if (source instanceof URL) {
            return true;
        }
        if (source instanceof URI) {
            return true;
        }
        if (source instanceof String) {
            try {
                URLUtil.url((String) source);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    @Override
    public FileWrapper getFileWrapper(Object source, String name, String contentType, Long size) throws IOException {

        if (source instanceof UriFileWrapper uriFileWrapper) {
            return updateFileWrapper(uriFileWrapper, name, contentType, size);
        }

        URL url;
        if (source instanceof URI uri) {
            url = uri.toURL();
        } else if (source instanceof String urlString) {
            url = URLUtil.url(urlString);
        } else if (source instanceof URL url1) {
            url = url1;
        } else {
            throw new UnsupportedOperationException();
        }

        URLConnection conn = url.openConnection();
        InputStream inputStream = IoUtil.toMarkSupportStream(conn.getInputStream());


        if (name == null) name = getName(conn, url);
        // 文件名称
        name = StrUtil.isEmpty(name) ? getName(conn, url) : name;
        // 大小
        if (Objects.isNull(size)) {
            size = conn.getContentLengthLong() < 0 ? null :conn.getContentLengthLong();
        }

        // 文件类型
        contentType = StrUtil.isEmpty(contentType) ? contentTypeDetect.detect(inputStream, name) : contentType;

        return new UriFileWrapper(inputStream, name, contentType, size);
    }


    public String getName(URLConnection conn, URL url) {
        String name = "";
        String disposition = conn.getHeaderField("Content-Disposition");

        if (StrUtil.isNotBlank(disposition)) {
            name = ReUtil.get("filename=\"(.*?)\"", disposition, 1);
            if (StrUtil.isBlank(name)) {
                name = StrUtil.subAfter(disposition, "filename=", true);
            }
        }

        if (StrUtil.isBlank(name)) {
            final String path = url.getPath();
            name = StrUtil.subSuf(path, path.lastIndexOf('/') + 1);
            if (StrUtil.isNotBlank(name)) {
                name = URLUtil.decode(name, StandardCharsets.UTF_8);
            }
        }
        return name;
    }

}
