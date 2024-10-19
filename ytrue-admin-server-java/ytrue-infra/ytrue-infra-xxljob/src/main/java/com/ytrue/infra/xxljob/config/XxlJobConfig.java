package com.ytrue.infra.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ytrue
 * @date 2023-09-07 14:57
 * @description XxlJob 配置类
 */
@Slf4j
@Configuration
public class XxlJobConfig {

    @Resource
    private XxlJobProperties xxlJobProperties; // 注入 XxlJobProperties 属性

    /**
     * 创建 XxlJobSpringExecutor 的 Bean
     *
     * @return XxlJobSpringExecutor 实例
     */
    @Bean
    @ConditionalOnProperty(name = "xxl.job.enable", havingValue = "true") // 只有在 xxl.job.enable 为 true 时才会创建该 Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init."); // 记录初始化日志

        // 创建 XxlJobSpringExecutor 实例
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        // 设置管理员地址
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
        // 设置应用名称
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getExecutor().getAppName());
        // 设置执行器地址
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
        // 设置 IP 地址
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
        // 设置端口
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
        // 设置访问令牌
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        // 设置日志路径
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
        // 设置日志保留天数
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());

        return xxlJobSpringExecutor; // 返回 XxlJobSpringExecutor 实例
    }
}
