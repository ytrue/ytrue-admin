package com.ytrue.tools.storage.platform;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.StorageFactory;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description IStorage
 */
public abstract class AbstractStorage implements IStorage, InitializingBean {

    @Override
    public FileInfo upload(UploadInfo uploadInfo) {
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
     * 构建基本的fileInfo
     *
     * @param uploadInfo
     * @return
     */
    protected FileInfo buildFileInfo(UploadInfo uploadInfo) {
        MultipartFile file = uploadInfo.getFileWrapper();
        if (file == null) {
            throw new StorageRuntimeException("文件不允许为 null ！");
        }

        FileInfo fileInfo = new FileInfo();
        // 设置时间
        fileInfo.setCreateTime(LocalDateTimeUtil.now());
        // 设置文件大小
        fileInfo.setSize(file.getSize());
        // 文件的原生名
        fileInfo.setOriginalFilename(file.getOriginalFilename());
        // 设置文件后缀
        fileInfo.setExt(FileNameUtil.getSuffix(file.getOriginalFilename()));
        // 设置附加属性
        fileInfo.setAttr(uploadInfo.getAttr());
        // 设置path
        fileInfo.setPath(uploadInfo.getPath());

        // 设置文件名称
        if (StrUtil.isNotBlank(uploadInfo.getUploadFilename())) {
            fileInfo.setFileName(uploadInfo.getUploadFilename());
        } else {
            // 随机名称
            fileInfo.setFileName(IdUtil.objectId() + (StrUtil.isEmpty(fileInfo.getExt()) ? StrUtil.EMPTY : "." + fileInfo.getExt()));
        }

        // 设置文件Content-Type内容类型
        if (uploadInfo.getContentType() != null) {
            fileInfo.setContentType(uploadInfo.getContentType());
        } else if (uploadInfo.getFileWrapper().getContentType() != null) {
            fileInfo.setContentType(uploadInfo.getFileWrapper().getContentType());
        } else {
            Tika tika = new Tika();
            fileInfo.setContentType(tika.detect(fileInfo.getFileName()));
        }

        return fileInfo;
    }

    /**
     * 平台
     *
     * @return
     */
    protected abstract String platform();

    @Override
    public void afterPropertiesSet() {
        StorageFactory.putMap(platform(), this);
    }
}
