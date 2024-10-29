package com.ytrue.infra.news;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息类，用于存储和传递文件的相关信息。
 * 实现了 Serializable 接口，以支持对象的序列化。
 *
 * @author ytrue
 * @date 2024/10/29
 */
@Data
public class FileMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -6604255398447349432L; // 序列化版本UID

    /**
     * 文件访问地址。
     * 用于访问和下载文件的 URL。
     */
    private String url;

    /**
     * 文件大小，单位字节。
     * 表示文件的字节数，用于了解文件的大小。
     */
    private Long size;

    /**
     * 文件名称。
     * 文件在存储系统中的名称，用于识别文件。
     */
    private String filename;

    /**
     * 原始文件名。
     * 用户上传文件时的原始名称，通常用于展示。
     */
    private String originalFilename;

    /**
     * 基础存储路径。
     * 文件在存储系统中的基础路径，通常是存储根目录。
     */
    private String basePath;

    /**
     * 存储路径。
     * 文件在存储系统中的具体存储路径，包含基础路径和文件名。
     */
    private String path;

    /**
     * 文件扩展名。
     * 表示文件的类型，例如 ".jpg"、".pdf" 等，通常用于文件处理和展示。
     */
    private String ext;

    /**
     * MIME 类型。
     * 表示文件的内容类型，例如 "image/jpeg"、"application/pdf" 等，用于文件的处理。
     */
    private String contentType;

    /**
     * 存储平台。
     * 表示文件存储的位置，例如 "LOCAL"、"OSS"、"COS" 等，指明文件的存储服务提供者。
     */
    private String platform;

    /**
     * 创建时间。
     * 文件的创建时间，用于记录文件的上传或生成时间。
     */
    private LocalDateTime createTime;
}
