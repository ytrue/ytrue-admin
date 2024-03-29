package com.ytrue.tools.storage.platform;

import cn.hutool.core.io.FileUtil;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import com.ytrue.tools.storage.utils.PathUtil;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description IStorage   TODO linux待测试
 */
public class LocalStorage extends AbstractStorage {

    private final LocalStorageProperties config;

    /**
     * 固定目录
     */
    private static final String FIXED_DIRECTORY = "/static";

    public LocalStorage(LocalStorageProperties config) {
        this.config = config;
    }

    @Override
    public FileInfo upload(UploadInfo uploadInfo) {
        FileInfo fileInfo = buildFileInfo(uploadInfo);
        fileInfo.setPlatform(platform());

        // 获取path xxxx\target\test-classes\storage
        String absoluteFilePath = getUploadPath(config.getFileHost());

        String path = fileInfo.getPath();
        File newFile = FileUtil.touch(PathUtil.montagePath(absoluteFilePath, path), fileInfo.getFileName());

        fileInfo.setBasePath(config.getFileHost());
        fileInfo.setUrl(PathUtil.montagePath(config.getDomain(), config.getFileHost(), path, fileInfo.getFileName()));

        try {
            FileUtil.writeBytes(uploadInfo.getFileWrapper().getBytes(), PathUtil.montagePath(absoluteFilePath, path, fileInfo.getFileName()));
        } catch (IOException e) {
            FileUtil.del(newFile);
            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileInfo.getOriginalFilename(), e);
        }
        return fileInfo;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return new File(getUploadPath(PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath())), fileInfo.getFileName()).exists();
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return FileUtil.del(new File(getUploadPath(PathUtil.montagePath(fileInfo.getBasePath(), fileInfo.getPath())), fileInfo.getFileName()));
    }

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        // 待处理 TODO
        try (InputStream in = FileUtil.getInputStream(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFileName())) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new StorageRuntimeException("文件下载失败！platform：" + fileInfo, e);
        }
    }

    @Override
    public String platform() {
        return StorageType.local.name();
    }

    @SneakyThrows
    private String getUploadPath(String fileHost) {
        File file = new File(ResourceUtils.getURL("classpath:").getPath() + FIXED_DIRECTORY);
        if (!file.exists()) {
            file = new File("");
        }
        File uploadFile = new File(file.getAbsolutePath(), fileHost);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }
        return uploadFile.getAbsolutePath();
    }


}
