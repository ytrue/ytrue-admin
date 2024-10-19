package com.ytrue.infra.mysql.query;

import com.ytrue.infra.mysql.query.aspect.MpQueryLimitAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 自动配置类，用于配置与 MyBatis Plus 查询限制相关的组件。
 *
 * @author ytrue
 * @date 2022/8/9 11:44
 */
@EnableConfigurationProperties(QueryProperties.class)
public class MpQueryAutoConfiguration {

    /**
     * 创建 MpQueryLimitAspect Bean，当配置属性
     * ytrue.query.mq-query-limit-enable 为 true 时启用。
     *
     * @return MpQueryLimitAspect 实例
     */
    @Bean
    @ConditionalOnProperty(name = "ytrue.query.mq-query-limit-enable", havingValue = "true", matchIfMissing = true)
    public MpQueryLimitAspect mpQueryLimitAspect() {
        return new MpQueryLimitAspect();
    }
}
