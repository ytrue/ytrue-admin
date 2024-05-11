package com.ytrue.infra.job;

import cn.hutool.core.collection.CollUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.ytrue.infra.job.handler.EveryDayExecute;
import com.ytrue.infra.job.handler.EveryHourExecute;
import com.ytrue.infra.job.handler.EveryMinuteExecute;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ytrue
 * @date 2023-09-07 15:06
 * @description TimedTaskJobHandler
 */
@Slf4j
@Component
public class TimedTaskJobHandler {

    @Autowired
    @Nullable
    private List<EveryMinuteExecute> everyMinuteExecutes;


    @Autowired
    @Nullable
    private List<EveryHourExecute> everyHourExecutes;


    @Autowired
    @Nullable
    private List<EveryDayExecute> everyDayExecutes;


    /**
     * 每分钟任务
     *
     * @throws Exception
     */
    @XxlJob("everyMinuteExecute")
    public void everyMinuteExecute() {
        log.info("每分钟任务执行");

        // 获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数...[{}]", param);

        if (CollUtil.isEmpty(everyDayExecutes)) {
            return;
        }
        assert everyMinuteExecutes != null;
        for (EveryMinuteExecute everyMinuteExecute : everyMinuteExecutes) {
            try {
                everyMinuteExecute.execute();
            } catch (Exception e) {
                log.error("每分钟任务异常", e);
            }
        }

    }

    /**
     * 每小时任务
     *
     * @throws Exception
     */
    @XxlJob("everyHourExecuteJobHandler")
    public void everyHourExecuteJobHandler() {
        log.info("每小时任务执行");

        // 获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数...[{}]", param);

        if (CollUtil.isEmpty(everyHourExecutes)) {
            return;
        }
        for (EveryHourExecute everyHourExecute : everyHourExecutes) {
            try {
                everyHourExecute.execute();
            } catch (Exception e) {
                log.error("每小时任务异常", e);
            }
        }
    }

    /**
     * 每日任务
     *
     * @throws Exception
     */
    @XxlJob("everyDayExecuteJobHandler")
    public void everyDayExecuteJobHandler() {
        log.info("每日任务执行");

        // 获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数...[{}]", param);

        if (CollUtil.isEmpty(everyDayExecutes)) {
            return;
        }
        for (EveryDayExecute everyDayExecute : everyDayExecutes) {
            try {
                everyDayExecute.execute();
            } catch (Exception e) {
                log.error("每日任务异常", e);
            }
        }

    }
}
