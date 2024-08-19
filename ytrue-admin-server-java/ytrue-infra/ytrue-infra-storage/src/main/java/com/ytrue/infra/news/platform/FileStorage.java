package com.ytrue.infra.news.platform;

import com.ytrue.infra.news.FileInfo;
import com.ytrue.infra.news.UploadTreatment;


public interface FileStorage {

    /**
     * 上传文件
     *
     * @param per
     * @return
     * @throws Exception
     */
    FileInfo save(UploadTreatment per) throws Exception;

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

}
