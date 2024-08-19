package com.ytrue.infra.storage.platform;



import com.ytrue.infra.storage.model.FileInfo;
import com.ytrue.infra.storage.model.UploadInfo;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description IStorage
 */
public interface IStorage {


    /**
     * 上传文件
     *
     * @param uploadInfo
     * @return
     */
    FileInfo upload(UploadInfo uploadInfo);

    /**
     * 判断文件是否存在
     *
     * @param fileInfo
     * @return
     */
    boolean exists(FileInfo fileInfo);

    /**
     * 删除文件
     *
     * @param fileInfo
     * @return
     */
    boolean delete(FileInfo fileInfo);

    /**
     * 下载
     *
     * @param fileInfo
     * @param consumer
     */
    void download(FileInfo fileInfo, Consumer<InputStream> consumer);


}
