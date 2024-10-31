//package com.ytrue.infra.storage.platform;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import com.ytrue.infra.storage.entity.FileMetadata;
//import com.ytrue.infra.storage.entity.UploadFileContext;
//import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
//import com.ytrue.infra.storage.util.MimeTypeUtil;
//import com.ytrue.infra.storage.util.PathUtil;
//import com.ytrue.infra.storage.wrapper.FileWrapper;
//import lombok.SneakyThrows;
//import org.springframework.util.ResourceUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDateTime;
//
//public class LocalFileStorage extends AbstractFileStorage {
//
//
//    /**
//     * 固定目录, springboot res/static
//     */
//    private static final String FIXED_DIRECTORY = "/static";
//
//    private static final String RESOURCE_LOCATION = "classpath:";
//
//    @Override
//    public FileMetadata save(UploadFileContext per) throws Exception {
//        // 获取文件包装器
//        FileWrapper fileWrapper = per.getFileWrapper();
//
//        FileMetadata fileMetadata = new FileMetadata();
//        // 设置时间
//        fileMetadata.setCreateTime(LocalDateTime.now());
//        // 设置文件大小
//        fileMetadata.setSize(fileWrapper.getSize());
//        // 文件的原生名
//        String originalFileName = fileWrapper.getName();
//        fileMetadata.setOriginalFilename(originalFileName);
//        // 设置文件后缀
//        String fileExtension = MimeTypeUtil.getPreferredFileExtension(originalFileName, fileWrapper.getContentType());
//        fileMetadata.setExt(fileExtension);
//        // contentType
//        fileMetadata.setContentType(fileWrapper.getContentType());
//
//        // 设置文件名称
//        String newFileName = per.getSaveFileName();
//        // 如果文件名称为空，这里随机生产名称
//        if (StrUtil.isBlank(newFileName)) {
//            newFileName = IdUtil.objectId() + (StrUtil.isBlank(fileMetadata.getExt()) ? StrUtil.EMPTY : fileMetadata.getExt());
//        }
//        fileMetadata.setFilename(newFileName);
//        // 设置平台
//        fileMetadata.setPlatform(platform());
//
//        // url  basePath path
//        String fileHost = "";
//        fileMetadata.setBasePath(fileHost);
//        fileMetadata.setPath(fileHost + per.getSavePath());
//
//
//        // 拼接一下
//        File saveFile = new File(getUploadPath(fileHost), per.getSavePath());
//        saveFile = new File(saveFile.getAbsolutePath(), fileMetadata.getFilename());
//
//        try {
//            // 获取文件流
//            InputStream inputStream = fileWrapper.getInputStream();
//            // 文件写入
//            FileUtil.writeBytes(inputStream.readAllBytes(), saveFile);
//        } catch (IOException e) {
//            FileUtil.del(saveFile);
//            throw new StorageRuntimeException("文件上传失败！platform：" + platform() + "，filename：" + fileMetadata.getOriginalFilename(), e);
//        }
//
//        return fileMetadata;
//    }
//
//    @Override
//    public boolean exists(FileMetadata fileMetadata) {
//        return new File(
//                getUploadPath(
//                        PathUtil.montagePath(
//                                fileMetadata.getBasePath(),
//                                fileMetadata.getPath())
//                ),
//                fileMetadata.getFilename()
//        ).exists();
//    }
//
//    @Override
//    public boolean delete(FileMetadata fileMetadata) {
//        return FileUtil.del(
//                new File(
//                        getUploadPath(
//                                PathUtil.montagePath(
//                                        fileMetadata.getBasePath(),
//                                        fileMetadata.getPath())
//                        ),
//                        fileMetadata.getFilename()
//                )
//        );
//    }
//
//
//    /**
//     * 获取文件上传路径的工具方法。
//     * 该方法根据指定的文件主机路径，返回上传文件的绝对路径。
//     * 如果指定的路径不存在，将创建相应的目录。
//     *
//     * @param fileHost 文件主机路径，如果为空则使用默认路径
//     * @return 返回创建或指定的上传路径的绝对路径
//     */
//    @SneakyThrows
//    private String getUploadPath(String fileHost) {
//        // 获取固定目录的文件对象
//        File file = new File(ResourceUtils.getURL(RESOURCE_LOCATION).getPath() + FIXED_DIRECTORY);
//
//        // 如果固定目录不存在，则使用空文件对象
//        if (!file.exists()) {
//            file = new File(StrUtil.EMPTY);
//        }
//
//        // 如果指定的文件主机路径不为空
//        if (!StrUtil.isBlank(fileHost)) {
//            // 在固定目录下创建一个以文件主机路径为名的目录
//            File uploadFilePath = new File(file.getAbsolutePath(), fileHost);
//            // 创建目录并返回其绝对路径
//            return FileUtil.mkdir(uploadFilePath).getAbsolutePath();
//        }
//
//        // 如果文件主机路径为空，直接创建固定目录并返回其绝对路径
//        return FileUtil.mkdir(file).getAbsolutePath();
//    }
//
//    /**
//     * 获取当前文件存储平台的名称。
//     *
//     * @return 返回存储平台的名称，使用枚举表示
//     */
//    @Override
//    public String platform() {
//        return FileStoragePlatformEnum.LOCAL.name();
//    }
//}
