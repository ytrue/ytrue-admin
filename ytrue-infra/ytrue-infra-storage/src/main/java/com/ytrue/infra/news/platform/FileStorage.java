package com.ytrue.infra.news.platform;

import com.ytrue.infra.storage.model.FileInfo;
import com.ytrue.infra.storage.model.UploadInfo;

import java.io.InputStream;
import java.util.function.Consumer;

public interface FileStorage {

    /**
     * 上传文件
     *
     * @param uploadInfo
     * @return
     */
    FileInfo save(UploadInfo uploadInfo);

    /**
     * 判断文件是否存在
     *
     * @param fileInfo
     * @return
     */
    boolean exists(FileInfo fileInfo);

    /**
     * 下载
     *
     * @param fileInfo
     * @param consumer
     */
    void download(FileInfo fileInfo, Consumer<InputStream> consumer);

    /**
     * 删除文件
     *
     * @param fileInfo
     * @return
     */
    boolean delete(FileInfo fileInfo);
}
