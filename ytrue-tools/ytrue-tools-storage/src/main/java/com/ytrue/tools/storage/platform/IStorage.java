package com.ytrue.tools.storage.platform;

import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.UploadInfo;
import org.springframework.beans.factory.InitializingBean;

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
