package com.ytrue.infra.operationlog;


import com.ytrue.infra.operationlog.aspect.OperateLogAspect;
import com.ytrue.infra.operationlog.properties.OperateLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/6/1 15:02
 * @description LogAutoConfiguration
 * 启动条件：
 * 1，存在web环境
 * 2，配置文件中      ytrue.log.enabled=true ,默认是开启的
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(OperateLogProperties.class)
@ConditionalOnProperty(name = "ytrue.operationlog.enabled", havingValue = "true", matchIfMissing = true)
public class OperationlogAutoConfiguration {

    /**
     * 注册
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }
}
