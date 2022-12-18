package com.ytrue.modules.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.quartz.model.ScheduleJobLog;
import com.ytrue.modules.quartz.service.IScheduleJobLogService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    private final IScheduleJobLogService scheduleJobLogService;

    /**
     * 定时日志列表
     *
     * @param pageQueryEntity
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("分页")
    public ApiResultResponse<IPage<ScheduleJobLog>> page(@RequestBody(required = false) PageQueryEntity<ScheduleJobLog> pageQueryEntity) {
        IPage<ScheduleJobLog> page = scheduleJobLogService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        scheduleJobLogService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }

    @DeleteMapping("clear")
    @ApiOperation("清空")
    public ApiResultResponse<Object> clearEx() {
        scheduleJobLogService.remove(Wrappers.emptyWrapper());
        return ApiResultResponse.success();
    }
}
