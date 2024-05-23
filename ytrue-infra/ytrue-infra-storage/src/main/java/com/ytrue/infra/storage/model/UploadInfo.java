package com.ytrue.infra.storage.model;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import com.ytrue.infra.storage.exception.StorageRuntimeException;
import lombok.Getter;
import lombok.Setter;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description UploadInfo
 */
@Getter
@Setter
public class UploadInfo {

    /**
     * 要上传的文件包装类
     */
    private MultipartFileWrapper fileWrapper;

    /**
     * 保存文件名，如果不设置则自动生成
     */
    private String uploadFilename;


    /**
     * MIME 类型，如果不设置则在上传文件根据 {@link MultipartFileWrapper#getContentType()} 和文件名自动识别
     */
    private String contentType;

    /**
     * 附加属性字典
     */
    private Dict attr;

    /**
     * 文件存储路径
     */
    private String path = "";

    /**
     * 根据MultipartFile构建
     *
     * @return
     */
    public static UploadInfo build(MultipartFile file) {
        UploadInfo uploadInfo = new UploadInfo();
        MultipartFileWrapper multipartFileWrapper = new MultipartFileWrapper(file);
        uploadInfo.setFileWrapper(multipartFileWrapper);
        uploadInfo.setContentType(multipartFileWrapper.getContentType());
        return uploadInfo;
    }

    /**
     * 根据InputStream构建
     *
     * @param inputStream
     * @return
     */
    public static UploadInfo build(InputStream inputStream) {
        try {
            return build(IoUtil.readBytes(inputStream));
        } catch (Exception e) {
            throw new StorageRuntimeException("根据 InputStream 创建UploadInfo失败！", e);
        }
    }

    /**
     * 根据byte[]构建
     *
     * @param bytes
     * @return
     */
    public static UploadInfo build(byte[] bytes) {
        String contentType = (new Tika()).detect(bytes);
        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile("", "", contentType, bytes)));
        uploadInfo.setContentType(contentType);
        return uploadInfo;
    }

    /**
     * 根据file构建
     *
     * @param file
     * @return
     */
    public static UploadInfo build(File file) {
        try {
            String contentType = (new Tika()).detect(file);
            UploadInfo uploadInfo = new UploadInfo();
            uploadInfo.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile(file.getName(), file.getName(), contentType, Files.newInputStream(file.toPath()))));
            uploadInfo.setContentType(contentType);
            return uploadInfo;
        } catch (Exception e) {
            throw new StorageRuntimeException("根据 File 创建UploadInfo失败！", e);
        }
    }

}
