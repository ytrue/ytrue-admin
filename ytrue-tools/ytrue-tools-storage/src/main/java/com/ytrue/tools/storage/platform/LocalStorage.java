package com.ytrue.tools.storage.platform;

import cn.hutool.core.io.FileUtil;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description IStorage
 */

@Component
@AllArgsConstructor
public class LocalStorage extends AbstractStorage {

    private final LocalStorageProperties config;

    @Override
    public FileInfo upload(UploadInfo uploadInfo) {
        FileInfo fileInfo = buildFileInfo(uploadInfo);
        fileInfo.setPlatform(platform());


        String absoluteFilePath = getUploadPath(config.getFileHost());

        String path = fileInfo.getPath();
        File newFile = FileUtil.touch(absoluteFilePath + path, fileInfo.getFileName());
        fileInfo.setBasePath(config.getFileHost());
        fileInfo.setUrl(config.getDomain() + config.getFileHost() + path + fileInfo.getFileName());

        try {
            // 路径先不管它 TODO 待验证
            FileUtil.writeBytes(uploadInfo.getFileWrapper().getBytes(), absoluteFilePath + path + fileInfo.getFileName());
        } catch (IOException e) {
            FileUtil.del(newFile);
            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileInfo.getOriginalFilename(), e);
        }
        return fileInfo;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return new File(getUploadPath(fileInfo.getBasePath()) + fileInfo.getPath(), fileInfo.getFileName()).exists();
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return FileUtil.del(new File(getUploadPath(fileInfo.getBasePath()) + fileInfo.getPath(), fileInfo.getFileName()));
    }

    @Override
    protected String platform() {
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
