package com.ytrue.infra.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ytrue
 * @date 2023-09-07 14:59
 * @description XxlJobProperties
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * 是否启动xxl-job
     */
    private boolean enable;

    /**
     * 调度中心配置
     */
    private Admin admin;

    /**
     * token
     */
    private String accessToken;

    /**
     * 执行器配置
     */
    private Executor executor;

    @Data
    public static class Admin {
        /**
         * 调度中心地址
         */
        private String addresses;

    }

    @Data
    public static class Executor {

        /**
         * 执行器注册名称
         */
        private String appName;


        /**
         * 执行器注册地址
         */
        private String address;


        /**
         * 执行器注册ip
         */
        private String ip;


        /**
         * 执行器 netty http server 端口
         */
        private int port;


        /**
         * 执行器日志路径
         */
        private String logPath;


        /**
         * 执行器日志保存期限
         */
        private int logRetentionDays;
    }

}
