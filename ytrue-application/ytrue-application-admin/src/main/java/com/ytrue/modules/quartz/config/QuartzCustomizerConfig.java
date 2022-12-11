package com.ytrue.modules.quartz.config;

import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description 当调度器shutdown被调用时等待当前被调度的任务完成
 */
@Configuration
public class QuartzCustomizerConfig implements SchedulerFactoryBeanCustomizer {

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        //当调度器shutdown被调用时等待当前被调度的任务完成
        schedulerFactoryBean
                .setWaitForJobsToCompleteOnShutdown(true);
    }
}
