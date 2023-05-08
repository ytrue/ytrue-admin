package com.ytrue.tools.storage;

import com.ytrue.tools.storage.platform.IStorage;
import lombok.Getter;

/**
 * @author ytrue  TODO 待完善
 * @date 2023/5/7 18:05
 * @description GenericStorageImpl
 */
public class GenericStorageImpl implements IStorage {

    @Getter
    private final IStorage originalStorage;

    public GenericStorageImpl(IStorage storage) {
        this.originalStorage = storage;
    }

    @Override
    public FileInfo upload(UploadInfo uploadInfo) {
        return originalStorage.upload(uploadInfo);
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return originalStorage.exists(fileInfo);
    }

    /**
     * 根据url判断是否存在
     *
     * @param url
     * @return
     */
    public boolean exists(String url) {
        return false;
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return originalStorage.delete(fileInfo);
    }

    /**
     * 根据url删除
     *
     * @param url
     * @return
     */
    public boolean delete(String url) {
        return false;
    }
}
