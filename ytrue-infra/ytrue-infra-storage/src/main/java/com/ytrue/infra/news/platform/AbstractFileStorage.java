package com.ytrue.infra.news.platform;

import com.ytrue.infra.storage.model.FileInfo;
import com.ytrue.infra.storage.model.UploadInfo;

import java.io.InputStream;
import java.util.function.Consumer;

public abstract class AbstractFileStorage  implements FileStorage{
    @Override
    public FileInfo save(UploadInfo uploadInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        throw new UnsupportedOperationException();
    }
}
