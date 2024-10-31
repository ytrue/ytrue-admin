package com.ytrue;

import com.ytrue.infra.storage.builder.FileMetadataBuilder;
import com.ytrue.infra.storage.FileStorageFactory;
import com.ytrue.infra.storage.builder.FileWrapperBuilder;
import com.ytrue.infra.storage.builder.UploadFileContextBuilder;
import com.ytrue.infra.storage.entity.FileMetadata;
import com.ytrue.infra.storage.entity.UploadFileContext;
import com.ytrue.infra.storage.platform.FileStorage;
import com.ytrue.infra.storage.wrapper.FileWrapper;
import com.ytrue.infra.storage.wrapper.adapter.LocalFileWrapperAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;

/**
 * @author ytrue
 * @description: AdminApplication
 * @date 2022/12/6 15:47
 */
@SpringBootApplication
public class AdminApplication {

    /**
     * 入口函数
     *
     * @param args 参数
     */
    public static void main(String[] args) throws Exception {

        // 关于java9对反射的限制,mybatisplus
        //  --add-opens java.base/java.lang.reflect=ALL-UNNAMED
        // BeanCopier 这个也要打破模块封装
        // --add-opens java.base/java.lang=ALL-UNNAMED
        // --add-opens java.base/java.lang.invoke=ALL-UNNAMED

        /*
         * 指定使用的日志框架，否则将会告警
         * RocketMQLog:WARN No appenders could be found for logger (io.netty.util.internal.InternalThreadLocalMap).
         * RocketMQLog:WARN Please initialize the logger system properly.
         */
        //System.setProperty("rocketmq.client.logUseSlf4j", "true");
        // 设置spring security 多线程获取SecurityContextHolder,这块改到 SecurityAutoConfiguration#initializingBean
        // System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");

//        ApplicationContext applicationContext = SpringApplication.run(AdminApplication.class, args);
//
//
//        FileStorage qiniu = FileStorageFactory.getFileStorage("tencent");
//        File file1 = new File("C:\\Users\\Administrator\\Desktop\\logo\\test.png");
//
//
//        UploadFileContext uploadFileContext = new UploadFileContextBuilder()
//                .withSource(file1).build();
//
//        FileMetadata fileMetadata = qiniu.save(uploadFileContext);
//        System.out.println(fileMetadata);
//
//       // String url = "http://sm5ndc3wi.hn-bkt.clouddn.com/672339f2d3579da2c1010551.png";
//        String url = fileMetadata.getUrl();
//        FileMetadata fileMetadata2 =   FileMetadataBuilder.fromUrl(url);
//        System.out.println(fileMetadata2);
//
//        System.out.println(qiniu.exists(fileMetadata2));
//        System.out.println(qiniu.delete(fileMetadata2));
//        System.out.println(qiniu.exists(fileMetadata2));




//        FileWrapperAdapter fileWrapperAdapter = new LocalFileWrapperAdapter();
//        FileWrapper fileWrapper = fileWrapperAdapter.getFileWrapper(file1);
//


        //
        // System.out.println(qiniu.save(uploadFileContext));


//        FileWrapper fileWrapper = new FileWrapperBuilder().withSource(file1).build();
//
//        UploadFileContext uploadFileContext = new UploadFileContext();
//        uploadFileContext.setFileWrapper(fileWrapper);
//        uploadFileContext.setSavePath("");
//        uploadFileContext.setSaveFileName("");

//
//        String url = "http://sm5ndc3wi.hn-bkt.clouddn.com/672329ffd3572a323d3d7c58.png";
//        FileMetadata fileMetadata = FileMetadataBuilder.fromUrl(url);
//
//        System.out.println(qiniu.exists(fileMetadata));
//        System.out.println(qiniu.delete(fileMetadata));
//        System.out.println(qiniu.exists(fileMetadata));

    }
}
