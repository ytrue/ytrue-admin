package com.ytrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    public static void main(String[] args) {

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
        SpringApplication.run(AdminApplication.class, args);
    }
}
