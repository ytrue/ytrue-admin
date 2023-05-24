package com.ytrue.tools.storage.test;

import com.ytrue.tools.storage.*;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import com.ytrue.tools.storage.utils.PathUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

public class TestStorage {

    @Test
    public void testPathUtil() {

        PathUtil.montagePath("/test", "test", "test/", "/test/test/");
        PathUtil.montagePath("\\test", "test", "test\\", "\\test/test\\");
        PathUtil.montagePath("\\test", "test", "test\\", "\\test\\test\\");
    }


    @Test
    public void test01() throws FileNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, InvocationTargetException, NoSuchMethodException {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);



        // LocalStorage localStorage = ac.getBean("localStorage", LocalStorage.class);
        File file = new File("C:\\Users\\Administrator\\Desktop\\TEST\\1111.jpg");
        UploadInfo uploadInfo = UploadInfo.build(file);

        GenericStorageImpl storage = StorageFactory.getInstance(StorageType.local.name(), new LocalStorageProperties());




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
