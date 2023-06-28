package com.ytrue.modules.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.db.entity.Pageable;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.quartz.model.query.ScheduleJobSearchQuery;
import com.ytrue.modules.quartz.model.po.ScheduleJob;
import com.ytrue.modules.quartz.service.IScheduleJobService;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description 定时任务控制器
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "定时任务")
@RequestMapping("/sys/schedule")
public class ScheduleJobController {

    private final IScheduleJobService scheduleJobService;

    @GetMapping("page")
    @Operation(summary = "分页查询数据")
    @PreAuthorize("@pms.hasPermission('system:schedule:resume')")
    public ApiResultResponse<IPage<ScheduleJob>> page(ScheduleJobSearchQuery params, Pageable pageable) {

        LambdaQueryWrapper<ScheduleJob> queryWrapper = QueryHelp.<ScheduleJob>lambdaQueryWrapperBuilder(params)
                .orderByDesc(ScheduleJob::getId);

        return ApiResultResponse.success(scheduleJobService.page(pageable.page(), queryWrapper));
    }


    @GetMapping("detail/{id}")
    @Operation(summary = "定时任务信息")
    @PreAuthorize("@pms.hasPermission('system:schedule:detail')")
    public ApiResultResponse<ScheduleJob> info(@PathVariable("id") Long id) {
        return ApiResultResponse.success(scheduleJobService.getById(id));
    }


    @PostMapping
    @Operation(summary = "保存定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:add')")
    public ApiResultResponse<Object> add(@Validated @RequestBody ScheduleJob scheduleJob) {

        long dbAlikeCount = scheduleJobService.count(
                Wrappers.<ScheduleJob>lambdaQuery()
                        .eq(ScheduleJob::getBeanName, scheduleJob.getBeanName())
                        .eq(ScheduleJob::getMethodName, scheduleJob.getMethodName())
        );

        AssertUtils.lessThanEq(dbAlikeCount, 0L, ResponseCode.SCHEDULED_TASK_EXISTS);
        scheduleJobService.saveAndStart(scheduleJob);
        return ApiResultResponse.success();
    }


    @PutMapping
    @Operation(summary = "修改定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:update')")
    public ApiResultResponse<Object> update(@Validated @RequestBody ScheduleJob scheduleJob) {

        long dbAlikeCount = scheduleJobService.count(
                Wrappers.<ScheduleJob>lambdaQuery()
                        .eq(ScheduleJob::getBeanName, scheduleJob.getBeanName())
                        .eq(ScheduleJob::getMethodName, scheduleJob.getMethodName())
                        .ne(ScheduleJob::getId, scheduleJob.getId())
        );
        AssertUtils.lessThanEq(dbAlikeCount, 0L, ResponseCode.SCHEDULED_TASK_EXISTS);
        scheduleJobService.updateScheduleJob(scheduleJob);
        return ApiResultResponse.success();
    }


    @DeleteMapping
    @Operation(summary = "删除定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:delete')")
    public ApiResultResponse<Object> delete(@RequestBody Long[] jobIds) {
        scheduleJobService.deleteBatch(jobIds);
        return ApiResultResponse.success();
    }

    @PostMapping("run")
    @Operation(summary = "立即执行任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:run')")
    public ApiResultResponse<Object> run(@RequestBody Long[] jobIds) {
        scheduleJobService.run(jobIds);
        return ApiResultResponse.success();
    }


    @PostMapping("pause")
    @Operation(summary = "暂停定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:pause')")
    public ApiResultResponse<Object> pause(@RequestBody Long[] jobIds) {
        scheduleJobService.pause(jobIds);
        return ApiResultResponse.success();
    }


    @PostMapping("resume")
    @Operation(summary = "恢复定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:resume')")
    public ApiResultResponse<Object> resume(@RequestBody Long[] jobIds) {
        scheduleJobService.resume(jobIds);
        return ApiResultResponse.success();
    }

}
