package com.ytrue.infra.mysql.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ytrue.infra.mysql.ExtendSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis Plus 的相关配置类。
 * <p>
 * 通过 @MapperScan 指定要生成实现类的接口所在的包，该包下的所有接口在编译后都会生成相应的实现类。
 * </p>
 *
 * @author ytrue
 * @date 2022/4/13 15:45
 */
@Configuration
@MapperScan(value = {"com.ytrue.repository.mysql"})
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * 自定义 SQL 注入器的 Bean。
     *
     * @return 扩展的 SQL 注入器
     */
    @Bean
    public ExtendSqlInjector extendSqlInjector() {
        return new ExtendSqlInjector();
    }

    /**
     * 自定义 MyBatis 配置以添加插件到 MyBatis 插件拦截链中。
     *
     * @return 配置自定义器
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 这里可以添加自定义拦截器，例如 ConditionInterceptor
            // configuration.addInterceptor(new ConditionInterceptor());
        };
    }

    /**
     * MyBatis Plus 插件配置。
     * <p>
     * 添加了乐观锁和分页插件到拦截器链中。
     * </p>
     *
     * @return MyBatis Plus 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 添加乐观锁插件
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 添加分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
