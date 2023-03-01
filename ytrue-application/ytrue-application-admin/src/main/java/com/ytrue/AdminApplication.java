package com.ytrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author ytrue
 * @description: AdminApplication
 * @date 2022/12/6 15:47
 */
@SpringBootApplication(scanBasePackages = "com.ytrue")
public class AdminApplication {

    /**
     * 入口函数
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
