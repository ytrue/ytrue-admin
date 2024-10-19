package com.ytrue.infra.xxljob;

import cn.hutool.core.collection.CollUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import com.ytrue.infra.xxljob.handler.EveryDayExecute;
import com.ytrue.infra.xxljob.handler.EveryHourExecute;
import com.ytrue.infra.xxljob.handler.EveryMinuteExecute;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ytrue
 * @date 2023-09-07 15:06
 * @description 定时任务处理器
 */
@Slf4j
@Component
public class TimedTaskJobHandler {

    @Autowired
    @Nullable
    private List<EveryMinuteExecute> everyMinuteExecutes; // 每分钟执行的任务列表


    @Autowired
    @Nullable
    private List<EveryHourExecute> everyHourExecutes; // 每小时执行的任务列表


    @Autowired
    @Nullable
    private List<EveryDayExecute> everyDayExecutes; // 每天执行的任务列表


    /**
     * 每分钟任务
     *
     * @throws Exception 可能抛出异常
     */
    @XxlJob("everyMinuteExecute")
    public void everyMinuteExecute() {
        log.info("每分钟任务执行");

        // 获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数...[{}]", param);

        if (CollUtil.isEmpty(everyMinuteExecutes)) {
            return; // 如果没有每分钟任务，直接返回
        }
        assert everyMinuteExecutes != null;
        for (EveryMinuteExecute everyMinuteExecute : everyMinuteExecutes) {
            try {
                everyMinuteExecute.execute(); // 执行每分钟任务
            } catch (Exception e) {
                log.error("每分钟任务异常", e); // 记录异常
            }
        }
    }

    /**
     * 每小时任务
     *
     * @throws Exception 可能抛出异常
     */
    @XxlJob("everyHourExecuteJobHandler")
    public void everyHourExecuteJobHandler() {
        log.info("每小时任务执行");

        // 获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数...[{}]", param);

        if (CollUtil.isEmpty(everyHourExecutes)) {
            return; // 如果没有每小时任务，直接返回
        }
        for (EveryHourExecute everyHourExecute : everyHourExecutes) {
            try {
                everyHourExecute.execute(); // 执行每小时任务
            } catch (Exception e) {
                log.error("每小时任务异常", e); // 记录异常
            }
        }
    }

    /**
     * 每日任务
     *
     * @throws Exception 可能抛出异常
     */
    @XxlJob("everyDayExecuteJobHandler")
    public void everyDayExecuteJobHandler() {
        log.info("每日任务执行");

        // 获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数...[{}]", param);

        if (CollUtil.isEmpty(everyDayExecutes)) {
            return; // 如果没有每日任务，直接返回
        }
        for (EveryDayExecute everyDayExecute : everyDayExecutes) {
            try {
                everyDayExecute.execute(); // 执行每日任务
            } catch (Exception e) {
                log.error("每日任务异常", e); // 记录异常
            }
        }
    }
}
