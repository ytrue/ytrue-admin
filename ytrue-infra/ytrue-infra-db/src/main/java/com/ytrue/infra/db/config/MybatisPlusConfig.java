package com.ytrue.infra.db.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ytrue.infra.db.ExtendSqlInjector;
import com.ytrue.tools.query.interceptor.ConditionInterceptor;
import com.ytrue.tools.security.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 * @author ytrue
 * @date 2022/4/13 15:45
 * @description mybatis plus的相关配置
 * @MapperScan 指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
 */
@Configuration
@MapperScan(value = {"com.ytrue.modules.*.dao", "com.ytrue.infra.db.dao"})
@EnableTransactionManagement
public class MybatisPlusConfig {


    @Bean
    public ExtendSqlInjector extendSqlInjector() {
        return new ExtendSqlInjector();
    }

    /**
     * 将插件加入到mybatis插件拦截链中
     *
     * @return
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.addInterceptor(new ConditionInterceptor());
    }


    /**
     * mybatis plus 插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 添加乐观锁
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 添加分页
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }


    /**
     * MetaObjectHandler
     */
    @Component
    public static class MpMetaObjectHandler implements MetaObjectHandler {


        private final static String UPDATE_TIME = "updateTime";
        private final static String CREATE_TIME = "createTime";
        private final static String CREATE_BY = "createBy";
        private final static String UPDATE_BY = "updateBy";
        private final static String PRIMARY_KEY = "id";

        /**
         * 新增时自动填充
         *
         * @param metaObject
         */
        @Override
        public void insertFill(MetaObject metaObject) {
            if (SecurityUtils.getAuthentication() != null) {
                if (SecurityUtils.isLogged()) {
                    setFieldValByName(CREATE_BY, SecurityUtils.getLoginUser().getUsername(), metaObject);
                }
            }
            setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
            setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);


//            // 关于主键的写入
//            if (metaObject.hasGetter(PRIMARY_KEY)) {
//                //如果已经配置id，则不再写入
//                if (metaObject.getValue(PRIMARY_KEY) == null) {
//                    // 还是数字
//                    this.setFieldValByName(PRIMARY_KEY, SnowFlake.getId(), metaObject);
//                }
//            }
        }

        /**
         * 更新时自动填充
         *
         * @param metaObject
         */
        @Override
        public void updateFill(MetaObject metaObject) {
            if (SecurityUtils.getAuthentication() != null) {
                if (SecurityUtils.isLogged()) {
                    setFieldValByName(UPDATE_BY, SecurityUtils.getLoginUser().getUsername(), metaObject);
                }
            }
            setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
        }
    }
}