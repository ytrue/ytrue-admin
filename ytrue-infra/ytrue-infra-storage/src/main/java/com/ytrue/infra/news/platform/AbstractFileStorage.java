package com.ytrue.infra.news.platform;

import com.ytrue.infra.news.FileInfo;
import com.ytrue.infra.news.UploadTreatment;


public abstract class AbstractFileStorage implements FileStorage {

    @Override
    public FileInfo save(UploadTreatment per) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        throw new UnsupportedOperationException();
    }

    /**
     * 平台
     *
     * @return
     */
    public abstract String platform();
}
