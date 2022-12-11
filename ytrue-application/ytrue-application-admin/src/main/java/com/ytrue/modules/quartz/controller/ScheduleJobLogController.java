package com.ytrue.modules.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.quartz.model.ScheduleJobLog;
import com.ytrue.modules.quartz.service.ScheduleJobLogService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description 定时任务日志控制器
 */
@RestController
@AllArgsConstructor
@Api(tags = "定时任务日志")
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {

    private final ScheduleJobLogService scheduleJobLogService;

    /**
     * 定时日志列表
     *
     * @param pageQueryEntity
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("分页查询数据")
    public ApiResultResponse<IPage<ScheduleJobLog>> page(@RequestBody(required = false) PageQueryEntity<ScheduleJobLog> pageQueryEntity) {

        IPage<ScheduleJobLog> page = scheduleJobLogService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }
}
