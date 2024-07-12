package com.ytrue.infra.news.platform;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.news.FileInfo;
import com.ytrue.infra.news.UploadTreatment;
import com.ytrue.infra.news.enums.FileStoragePlatformEnum;
import com.ytrue.infra.news.util.PathUtil;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.storage.exception.StorageRuntimeException;

import lombok.SneakyThrows;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

public class LocalFileStorage extends AbstractFileStorage {


    /**
     * 固定目录, springboot res/static
     */
    private static final String FIXED_DIRECTORY = "/static";

    private static final String RESOURCE_LOCATION = "classpath:";

    @Override
    public FileInfo save(UploadTreatment per) throws Exception {
        // 获取文件包装器
        FileWrapper fileWrapper = per.getFileWrapper();
        // 检查一下
        Assert.notNull(fileWrapper, "fileWrapper is null");

        FileInfo fileInfo = new FileInfo();
        // 设置时间
        fileInfo.setCreateTime(LocalDateTime.now());
        // 设置文件大小
        fileInfo.setSize(fileWrapper.getSize());
        // 文件的原生名
        String originalFileName = fileWrapper.getName();
        fileInfo.setOriginalFilename(originalFileName);
        // 设置文件后缀
        String fileExtension = com.ytrue.infra.news.util.FileUtil.getPreferredFileExtension(originalFileName, fileWrapper.getContentType());
        fileInfo.setExt(fileExtension);
        // contentType
        fileInfo.setContentType(fileWrapper.getContentType());

        // 设置文件名称
        String newFileName = per.getSaveFileName();
        // 如果文件名称为空，这里随机生产名称
        if (StrUtil.isBlank(newFileName)) {
            newFileName = IdUtil.objectId() + (StrUtil.isBlank(fileInfo.getExt()) ? StrUtil.EMPTY : fileInfo.getExt());
        }
        fileInfo.setFilename(newFileName);
        // 设置平台
        fileInfo.setPlatform(platform());

        // url  basePath path
        String fileHost = "";
        fileInfo.setBasePath(fileHost);
        fileInfo.setPath(fileHost + per.getSavePath());


        // 拼接一下
        File saveFile = new File(getUploadPath(fileHost), per.getSavePath());
        saveFile = new File(saveFile.getAbsolutePath(), fileInfo.getFilename());

        try {
            // 获取文件流
            InputStream inputStream = fileWrapper.getInputStream();
            // 文件写入
            FileUtil.writeBytes(inputStream.readAllBytes(), saveFile);
        } catch (IOException e) {
            FileUtil.del(saveFile);
            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileInfo.getOriginalFilename(), e);
        }

        return fileInfo;
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        return new File(
                getUploadPath(
                        PathUtil.montagePath(
                                fileInfo.getBasePath(),
                                fileInfo.getPath())
                ),
                fileInfo.getFilename()
        ).exists();
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        return FileUtil.del(
                new File(
                        getUploadPath(
                                PathUtil.montagePath(
                                        fileInfo.getBasePath(),
                                        fileInfo.getPath())
                        ),
                        fileInfo.getFilename()
                )
        );
    }


    @SneakyThrows
    private String getUploadPath(String fileHost) {
        File file = new File(ResourceUtils.getURL(RESOURCE_LOCATION).getPath() + FIXED_DIRECTORY);
        if (!file.exists()) {
            file = new File(StrUtil.EMPTY);
        }

        if (!StrUtil.isBlank(fileHost)) {
            File uploadFilePath = new File(file.getAbsolutePath(), fileHost);
            // 创建目录 and 返回绝对路径
            return FileUtil.mkdir(uploadFilePath).getAbsolutePath();
        }

        // 创建目录 and 返回绝对路径
        return FileUtil.mkdir(file).getAbsolutePath();
    }

    @Override
    public String platform() {
        return FileStoragePlatformEnum.LOCAL.name();
    }

}
