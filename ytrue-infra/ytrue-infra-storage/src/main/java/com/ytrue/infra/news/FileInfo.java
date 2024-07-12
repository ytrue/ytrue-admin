package com.ytrue.infra.news;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileInfo  implements Serializable {
    @Serial
    private static final long serialVersionUID = -6604255398447349432L;

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 文件大小，单位字节
     */
    private Long size;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 基础存储路径
     */
    private String basePath;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * MIME 类型
     */
    private String contentType;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
