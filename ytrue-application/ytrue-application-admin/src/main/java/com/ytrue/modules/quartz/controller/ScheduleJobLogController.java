package com.ytrue.modules.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.entity.Pageable;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.quartz.model.query.ScheduleJobLogQuery;
import com.ytrue.modules.quartz.model.po.ScheduleJobLog;
import com.ytrue.modules.quartz.service.IScheduleJobLogService;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description 定时任务日志控制器
 */
@RestController
@AllArgsConstructor
@Tag(name = "定时任务日志")
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {

    private final IScheduleJobLogService scheduleJobLogService;

    @GetMapping("/page")
    @Operation(summary="分页")
    @PreAuthorize("@pms.hasPermission('system:scheduleLog:page')")
    public ApiResultResponse<IPage<ScheduleJobLog>>  page(ScheduleJobLogQuery params, Pageable pageable) {

        LambdaQueryWrapper<ScheduleJobLog> queryWrapper = QueryHelp.<ScheduleJobLog>lambdaQueryWrapperBuilder(params)
                .orderByDesc(ScheduleJobLog::getId);

        return ApiResultResponse.success(scheduleJobLogService.page(pageable.page(), queryWrapper));
    }

    @DeleteMapping
    @Operation(summary="删除")
    @PreAuthorize("@pms.hasPermission('system:scheduleLog:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        scheduleJobLogService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }

    @DeleteMapping("clear")
    @Operation(summary="清空")
    @PreAuthorize("@pms.hasPermission('system:scheduleLog:clear')")
    public ApiResultResponse<Object> clear() {
        scheduleJobLogService.remove(Wrappers.emptyWrapper());
        return ApiResultResponse.success();
    }
}
