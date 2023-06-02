package com.ytrue.modules.common;

import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.tools.security.annotation.IgnoreWebSecurity;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.GenericStorageImpl;
import com.ytrue.tools.storage.StorageFactory;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.properties.AliyunOssStorageProperties;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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


    @PostMapping("upload")
    @Operation(summary = "文件上传")
    public ApiResultResponse<FileInfo> fileUpload(MultipartFile file) {
        GenericStorageImpl storage = StorageFactory.getInstance(StorageType.local.name());
        FileInfo fileInfo = storage.upload(UploadInfo.build(file));
        return ApiResultResponse.success(fileInfo);
    }
}
