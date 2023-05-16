package com.ytrue.tools.storage.platform;

import cn.hutool.core.io.FileUtil;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import com.ytrue.tools.storage.utils.PathUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description IStorage   TODO linux待测试
 */

@Component
@AllArgsConstructor
public class LocalStorage extends AbstractStorage {

    private final LocalStorageProperties config;

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
    public String platform() {
        return StorageType.local.name();
    }

    @SneakyThrows
    private String getUploadPath(String fileHost) {
        File file = new File(ResourceUtils.getURL("classpath:").getPath());
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
