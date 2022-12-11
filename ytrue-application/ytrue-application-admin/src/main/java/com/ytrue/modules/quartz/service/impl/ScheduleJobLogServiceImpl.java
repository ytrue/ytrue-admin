package com.ytrue.modules.quartz.service.impl;

import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.modules.quartz.dao.ScheduleJobLogDao;
import com.ytrue.modules.quartz.model.ScheduleJobLog;
import com.ytrue.modules.quartz.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description ScheduleJobLogServiceImpl
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends BaseServiceImpl<ScheduleJobLogDao, ScheduleJobLog> implements ScheduleJobLogService {
}
