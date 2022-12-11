package com.ytrue.modules.quartz.service.manager;


import com.ytrue.modules.quartz.config.QuartzJob;
import com.ytrue.modules.quartz.enums.ScheduleStatus;
import com.ytrue.modules.quartz.model.ScheduleJob;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description 定时任务工具类
 */
@Component
@AllArgsConstructor
public class ScheduleManager {

    private final static String JOB_NAME = "TASK_";

    private final Scheduler scheduler;

    /**
     * 获取触发器key
     *
     * @param scheduleJob
     * @return
     */
    private TriggerKey getTriggerKey(ScheduleJob scheduleJob) {
        return TriggerKey.triggerKey(JOB_NAME + scheduleJob.getId());
    }

    /**
     * 获取jobKey
     *
     * @param scheduleJob
     * @return
     */
    private JobKey getJobKey(ScheduleJob scheduleJob) {
        return JobKey.jobKey(JOB_NAME + scheduleJob.getId());
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduleJob
     * @return
     */
    public CronTrigger getCronTrigger(ScheduleJob scheduleJob) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new RuntimeException("获取定时任务CronTrigger出现异常", e);
        }
    }

    /**
     * 创建定时任务
     *
     * @param scheduleJob
     */
    public void createScheduleJob(ScheduleJob scheduleJob) {
        try {
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(getJobKey(scheduleJob)).build();

            //表达式调度构建器，可以根据scheduleJob修改withMisfireHandling方法，但是使用异步执行定时任务，没必要
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionFireAndProceed();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob)).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(QuartzJob.JOB_PARAM_KEY, scheduleJob);

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if (scheduleJob.getStatus().equals(ScheduleStatus.PAUSE.getType())) {
                pauseJob(scheduleJob);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("创建定时任务失败", e);
        }
    }

    /**
     * 更新定时任务
     *
     * @param scheduleJob
     */
    public void updateScheduleJob(ScheduleJob scheduleJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJob);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()).withMisfireHandlingInstructionFireAndProceed();

            CronTrigger trigger = getCronTrigger(scheduleJob);

            // 如果定时任务不存在，则创建定时任务
            if (trigger == null) {
                createScheduleJob(scheduleJob);
                return;
            }

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            //参数
            trigger.getJobDataMap().put(QuartzJob.JOB_PARAM_KEY, scheduleJob);

            scheduler.rescheduleJob(triggerKey, trigger);

            //暂停任务
            if (scheduleJob.getStatus().equals(ScheduleStatus.PAUSE.getType())) {
                pauseJob(scheduleJob);
            }

        } catch (SchedulerException e) {
            throw new RuntimeException("更新定时任务失败", e);
        }
    }

    /**
     * 立即执行任务
     *
     * @param scheduleJob
     */
    public void run(ScheduleJob scheduleJob) {
        try {
            //参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(QuartzJob.JOB_PARAM_KEY, scheduleJob);

            scheduler.triggerJob(getJobKey(scheduleJob), dataMap);
        } catch (SchedulerException e) {
            throw new RuntimeException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     *
     * @param scheduleJob
     */
    public void pauseJob(ScheduleJob scheduleJob) {
        try {
            scheduler.pauseJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     *
     * @param scheduleJob
     */
    public void resumeJob(ScheduleJob scheduleJob) {
        try {
            scheduler.resumeJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new RuntimeException("恢复定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     *
     * @param scheduleJob
     */
    public void deleteScheduleJob(ScheduleJob scheduleJob) {
        try {
            // 停止触发器
            scheduler.pauseTrigger(getTriggerKey(scheduleJob));
            //移除触发器
            scheduler.unscheduleJob(getTriggerKey(scheduleJob));
            //删除任务
            scheduler.deleteJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new RuntimeException("删除定时任务失败", e);
        }
    }

}
