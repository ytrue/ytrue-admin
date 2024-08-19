package com.ytrue.infra.db.query;

import com.ytrue.infra.db.query.aspect.MpQueryLimitAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/8/9 11:44
 * @description MpQueryAutoConfiguration
 */
@EnableConfigurationProperties(QueryProperties.class)
public class MpQueryAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "ytrue.query.mq-query-limit-enable", havingValue = "true", matchIfMissing = true)
    public MpQueryLimitAspect mpQueryLimitAspect() {
        return new MpQueryLimitAspect();
    }
}
