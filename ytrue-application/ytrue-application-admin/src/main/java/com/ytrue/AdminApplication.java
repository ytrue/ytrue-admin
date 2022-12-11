package com.ytrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ytrue
 * @description: AdminApplication
 * @date 2022/12/6 15:47
 */
@SpringBootApplication
@EnableTransactionManagement
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
