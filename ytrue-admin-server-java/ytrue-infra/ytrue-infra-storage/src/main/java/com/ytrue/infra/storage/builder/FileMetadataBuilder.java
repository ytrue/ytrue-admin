package com.ytrue.infra.storage.builder;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.storage.entity.FileMetadata;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

/**
 * FileMetadataBuilder 是一个构建器类，用于从给定的 URL 创建 FileMetadata 对象。
 *
 * <p>该类提供了静态方法，从 URL 解析文件的相关信息，如文件名、扩展名和存储路径。</p>
 *
 * @author ytrue
 * @date 2024/10/31 15:05
 */
public class FileMetadataBuilder {

    /**
     * 从给定的 URL 创建 FileMetadata 对象。
     *
     * @param url 文件的完整 URL 地址
     * @return FileMetadata 对象，包含从 URL 解析出的文件信息
     * @throws URISyntaxException 如果 URL 格式不正确
     */
    public static FileMetadata fromUrl(String url) throws URISyntaxException {
        // 解析 URL，获取其各个组成部分
        URI uri = new URI(url);

        // 获取完整路径部分
        String fullPath = uri.getPath();

        // 提取文件名，从完整路径中找到最后一个斜杠后的部分
        String filename = fullPath.substring(fullPath.lastIndexOf(StrPool.C_SLASH) + 1);

        // 获取文件扩展名，如果文件名中存在点，则提取后缀
        String ext = filename.contains(StrPool.DOT) ? filename.substring(filename.lastIndexOf(StrPool.C_DOT)) : StrUtil.EMPTY;

        // 初始化文件存储路径
        String path = null;
        // 如果路径中包含文件名之前的斜杠，则提取路径
        if (fullPath.lastIndexOf(StrPool.C_SLASH) > 1) {
            path = fullPath.substring(1, fullPath.lastIndexOf(StrPool.C_SLASH));
        }

        // 创建 FileMetadata 对象，用于存储文件的元数据信息
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setUrl(url);                     // 设置文件的访问 URL
        fileMetadata.setFilename(filename);           // 设置文件名
        fileMetadata.setExt(ext);                     // 设置文件扩展名

        // 设置文件的具体存储路径（目录）
        fileMetadata.setPath(path);

        // 设置其他属性，默认文件大小为 0 字节
        fileMetadata.setSize(0L);
        fileMetadata.setCreateTime(LocalDateTime.now()); // 设置文件创建时间为当前时间

        // 可以根据需要设置其他字段，例如内容类型、平台标识和基础路径等
        // 这里可以根据需要设置其他字段
        // fileMetadata.setContentType();
        // fileMetadata.setPlatform(platform);
        // fileMetadata.setBasePath(fileHost);
        // fileMetadata.setOriginalFilename(filename);

        return fileMetadata; // 返回构建好的 FileMetadata 对象
    }
}
