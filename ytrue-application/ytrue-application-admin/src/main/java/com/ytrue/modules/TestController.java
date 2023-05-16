package com.ytrue.modules;

import com.ytrue.tools.security.annotation.IgnoreWebSecurity;
import com.ytrue.tools.storage.FileInfo;
import com.ytrue.tools.storage.GenericStorageImpl;
import com.ytrue.tools.storage.StorageFactory;
import com.ytrue.tools.storage.UploadInfo;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("test")
public class TestController {


    @IgnoreWebSecurity
    @PostMapping("upload")
    @ResponseBody
    public String test(MultipartFile file) {

        GenericStorageImpl storage = StorageFactory.getInstance(StorageType.local.name(), new LocalStorageProperties());

        UploadInfo uploadInfo = UploadInfo.build(file);

        FileInfo upload = storage.upload(uploadInfo);

        System.out.println(upload);
        System.out.println(storage.exists(upload.getUrl()));
        System.out.println(storage.delete(upload.getUrl()));
        System.out.println(storage.exists(upload.getUrl()));

        return "ok";
    }
}
