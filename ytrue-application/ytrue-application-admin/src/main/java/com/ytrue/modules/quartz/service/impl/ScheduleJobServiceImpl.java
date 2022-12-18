package com.ytrue.modules.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.modules.quartz.dao.ScheduleJobDao;
import com.ytrue.modules.quartz.enums.ScheduleStatus;
import com.ytrue.modules.quartz.model.ScheduleJob;
import com.ytrue.modules.quartz.service.IScheduleJobService;
import com.ytrue.modules.quartz.service.manager.ScheduleManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description ScheduleJobServiceImpl
 */
@Service
@Slf4j
@AllArgsConstructor
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJobDao, ScheduleJob> implements IScheduleJobService, InitializingBean {


    private final ScheduleJobDao scheduleJobDao;

    private final ScheduleManager scheduleManager;


    /**
     * 初始化bean时，初始化定时器
     */
    @Override
    public void afterPropertiesSet() {
        list().forEach(scheduleJob -> {
            CronTrigger trigger = scheduleManager.getCronTrigger(scheduleJob);
            // 如果定时任务不存在，则创建定时任务
            if (trigger == null) {
                scheduleManager.createScheduleJob(scheduleJob);
            } else if (ScheduleStatus.NORMAL.getType().equals(scheduleJob.getStatus())) {
                scheduleManager.resumeJob(scheduleJob);
            } else if (ScheduleStatus.PAUSE.getType().equals(scheduleJob.getStatus())) {
                scheduleManager.pauseJob(scheduleJob);
            }
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndStart(ScheduleJob scheduleJob) {
        scheduleJob.setStatus(ScheduleStatus.NORMAL.getType());
        scheduleJobDao.insert(scheduleJob);

        scheduleManager.createScheduleJob(scheduleJob);
    }

    /**
     * 更新定时任务
     *
     * @param scheduleJob
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScheduleJob(ScheduleJob scheduleJob) {
        scheduleManager.updateScheduleJob(scheduleJob);
        scheduleJobDao.updateById(scheduleJob);
    }

    /**
     * 批量删除定时任务
     *
     * @param jobIds 需要删除的job id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
        List<Long> ids = Arrays.asList(jobIds);
        this.listByIds(ids).forEach(scheduleManager::deleteScheduleJob);
        scheduleJobDao.deleteBatchIds(ids);
    }


    /**
     * 批量更新定时任务状态
     *
     * @param jobIds 需要更新的job id列表
     * @param status 更新后的状态
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(Long[] jobIds, int status) {
        Arrays.asList(jobIds).forEach(jobId -> scheduleJobDao.update(null, Wrappers.<ScheduleJob>lambdaUpdate().eq(ScheduleJob::getId, jobId).set(ScheduleJob::getStatus, status)));


    }

    /**
     * 立即执行
     *
     * @param jobIds job id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
        for (Long jobId : jobIds) {
            scheduleManager.run(scheduleJobDao.selectById(jobId));
        }
    }

    /**
     * 暂停运行
     *
     * @param jobIds job id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        this.listByIds(Arrays.asList(jobIds)).forEach(scheduleManager::pauseJob);
        updateBatch(jobIds, ScheduleStatus.PAUSE.getType());
    }

    /**
     * 恢复运行
     *
     * @param jobIds job id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
        this.listByIds(Arrays.asList(jobIds)).forEach(scheduleManager::resumeJob);

        updateBatch(jobIds, ScheduleStatus.NORMAL.getType());
    }


}
