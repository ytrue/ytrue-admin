package com.ytrue.infra.storage.platform;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.storage.StorageFactory;
import com.ytrue.infra.storage.exception.StorageRuntimeException;
import com.ytrue.infra.storage.model.FileInfo;
import com.ytrue.infra.storage.model.UploadInfo;
import com.ytrue.infra.storage.utils.ContentTypeUtil;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;

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

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * 构建基本的fileInfo
     *
     * @param uploadInfo
     * @return
     */
    @SneakyThrows
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
        // 设置平台
        fileInfo.setPlatform(platform());
        // 设置文件Content-Type内容类型
        Tika tika1 = new Tika();
        fileInfo.setContentType(tika1.detect(uploadInfo.getFileWrapper().getInputStream()));

        // 设置文件后缀
        if (StrUtil.isBlank(fileInfo.getExt())) {
            String s = ContentTypeUtil.getFileType(fileInfo.getContentType()).get(0);
            fileInfo.setExt(s.substring(1));
        }

        // 设置文件名称
        if (StrUtil.isNotBlank(uploadInfo.getUploadFilename())) {
            fileInfo.setFileName(uploadInfo.getUploadFilename());
        } else {
            // 随机名称
            fileInfo.setFileName(IdUtil.objectId() + (StrUtil.isEmpty(fileInfo.getExt()) ? StrUtil.EMPTY : "." + fileInfo.getExt()));
        }

        /*if (uploadInfo.getContentType() != null) {
            fileInfo.setContentType(uploadInfo.getContentType());
        } else if (uploadInfo.getFileWrapper().getContentType() != null) {
            fileInfo.setContentType(uploadInfo.getFileWrapper().getContentType());
        } else {
            Tika tika = new Tika();
            fileInfo.setContentType(tika.detect(fileInfo.getFileName()));
        }*/


        return fileInfo;
    }

    // 文件后缀 ....
    private void getType(String mimeType) throws MimeTypeException {
        // 获取MIME类型对象
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType type = allTypes.forName(mimeType);
        // 获取文件后缀
        String[] extensions = type.getExtensions().toArray(new String[0]);
        System.out.println(Arrays.toString(extensions));
    }


    /**
     * 平台
     *
     * @return
     */
    public abstract String platform();

    @Override
    public void afterPropertiesSet() {
        StorageFactory.registerStorage(platform(), this);
    }
}
