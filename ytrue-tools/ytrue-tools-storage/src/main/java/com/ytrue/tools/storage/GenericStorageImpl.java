package com.ytrue.tools.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.ytrue.tools.storage.platform.AbstractStorage;
import com.ytrue.tools.storage.platform.IStorage;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

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

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        originalStorage.download(fileInfo, consumer);
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return originalStorage.delete(fileInfo);
    }


    /**
     * 根据url判断是否存在
     *
     * @param url
     * @return
     */
    public boolean exists(String url) {
        return originalStorage.exists(getFileInfo(url));
    }


    /**
     * 根据url删除
     *
     * @param url
     * @return
     */
    public boolean delete(String url) {
        return this.delete(getFileInfo(url));
    }


    /**
     * 根据url下载
     *
     * @param url
     * @param consumer
     */
    public void download(String url, Consumer<InputStream> consumer) {
        originalStorage.download(getFileInfo(url), consumer);
    }

    /**
     * 获取fileInfo
     *
     * @param url
     * @return
     */
    private FileInfo getFileInfo(String url) {
        String filePath = URLUtil.getPath(url);
        String fileName = FileUtil.getName(filePath);
        String path = StrUtil.subSuf(StrUtil.sub(filePath, 0, ~fileName.length()), 1);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(url);
        fileInfo.setPath(path);
        fileInfo.setFileName(fileName);
        fileInfo.setExt(FileUtil.getSuffix(fileName));
        // 设置平台
        if (originalStorage instanceof AbstractStorage) {
            fileInfo.setPlatform(((AbstractStorage) originalStorage).platform());
        }
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
        fileInfo.setContentType(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM).toString());
        return fileInfo;
    }
}
