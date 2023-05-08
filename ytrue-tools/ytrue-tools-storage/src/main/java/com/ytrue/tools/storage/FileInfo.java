package com.ytrue.tools.storage;

import cn.hutool.core.lang.Dict;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description FileInfo
 */
@Data
public class FileInfo implements Serializable {
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
    private String fileName;

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


    /**
     * 附加属性字典
     */
    private Dict attr;
}
