package com.ytrue.tools.storage.test;

import com.ytrue.tools.storage.*;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.platform.IStorage;
import com.ytrue.tools.storage.properties.TencentCosStorageProperties;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class TestStorage {

    @Test
    public void test01() throws FileNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);


//       // LocalStorage localStorage = ac.getBean("localStorage", LocalStorage.class);
//        File file = new File("C:\\Users\\Administrator\\Desktop\\测试目录\\my.png");
//        UploadInfo uploadInfo = UploadInfo.build(file);
//
//
//        IStorage localStorage = StorageFactory.getInstance(StorageType.cos.name(), new TencentCosStorageProperties());
//        FileInfo fileInfo = localStorage.upload(uploadInfo);
//
//        System.out.println(fileInfo);
//
//        System.out.println(localStorage.exists(fileInfo));
//        System.out.println(localStorage.delete(fileInfo));
//        System.out.println(localStorage.exists(fileInfo));

        //  System.out.println(upload);

    }


    @Test
    public void test02() throws FileNotFoundException {
        System.out.println(getUploadPath("C:\\DrvPath"));
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
