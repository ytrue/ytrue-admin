package com.ytrue.tools.storage.test;

import cn.hutool.core.io.FileUtil;
import com.ytrue.tools.storage.*;
import com.ytrue.tools.storage.enums.StorageType;
import com.ytrue.tools.storage.properties.AliyunOssStorageProperties;
import com.ytrue.tools.storage.properties.LocalStorageProperties;
import com.ytrue.tools.storage.utils.PathUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class TestStorage {

    @Test
    public void testPathUtil() {

        PathUtil.montagePath("/test", "test", "test/", "/test/test/");
        PathUtil.montagePath("\\test", "test", "test\\", "\\test/test\\");
        PathUtil.montagePath("\\test", "test", "test\\", "\\test\\test\\");
    }


    @Test
    public void test01() throws FileNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, InvocationTargetException, NoSuchMethodException {
        ApplicationContext ac = new AnnotationConfigApplicationContext(StorageAutoConfiguration.class);


        // LocalStorage localStorage = ac.getBean("localStorage", LocalStorage.class);
        File file = new File("C:\\Users\\Administrator\\Desktop\\TEST\\1111.jpg");
        UploadInfo uploadInfo = UploadInfo.build(file);

        GenericStorageImpl storage = StorageFactory.getInstance(StorageType.oss.name(), new AliyunOssStorageProperties());
        //System.out.println(storage.upload(uploadInfo));

        storage.download("xxx/64794ea6ab30a684262c280c.jpg", new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) {

                File file1 = new File("C:\\Users\\Administrator\\Desktop\\TEST\\YYYY.jpg");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);

                    byte flush[] = new byte[1024];
                    int len;
                    while (0 <= (len = inputStream.read(flush))) {
                        fileOutputStream.write(flush, 0, len);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
