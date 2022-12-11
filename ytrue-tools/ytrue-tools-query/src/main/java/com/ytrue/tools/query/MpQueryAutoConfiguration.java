package com.ytrue.tools.query;

import com.ytrue.tools.query.aspect.MpQueryLimitAspect;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/8/9 11:44
 * @description MpQueryAutoConfiguration
 */
public class MpQueryAutoConfiguration {

    @Bean
    public MpQueryLimitAspect mpQueryLimitAspect() {
        return new MpQueryLimitAspect();
    }
}
