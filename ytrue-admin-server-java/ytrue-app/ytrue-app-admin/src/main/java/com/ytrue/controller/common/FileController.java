package com.ytrue.controller.common;


import com.ytrue.infra.core.response.ServerResponseEntity;
//import com.ytrue.tools.storage.model.FileInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author ytrue
 * @date 2021/6/2 15:36
 * @description 文件上传
 */
@Tag(name = "文件管理")
@RestController
@RequestMapping("file")
public class FileController {


//    @PostMapping("upload")
//    @Operation(summary = "文件上传")
//    public ServerResponseEntity<FileInfo> fileUpload(MultipartFile file) {
//        // GenericStorageImpl storage = StorageFactory.getInstance(StorageType.local.name());
//        //  FileInfo fileInfo = storage.upload(UploadInfo.build(file));
//        // return ServerResponseEntity.success(fileInfo);
//
//        return null;
//    }
}
