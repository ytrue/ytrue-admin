package com.ytrue.modules.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.entity.Pageable;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.quartz.model.dto.ScheduleJobSearchParams;
import com.ytrue.modules.quartz.model.po.ScheduleJob;
import com.ytrue.modules.quartz.service.IScheduleJobService;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "定时任务")
@RequestMapping("/sys/schedule")
public class ScheduleJobController {

    private final IScheduleJobService scheduleJobService;

    @GetMapping("page")
    @ApiOperation("分页查询数据")
    @PreAuthorize("@pms.hasPermission('system:schedule:resume')")
    public ApiResultResponse<IPage<ScheduleJob>> page(ScheduleJobSearchParams params, Pageable pageable) {

        LambdaQueryWrapper<ScheduleJob> queryWrapper = QueryHelp.<ScheduleJob>lambdaQueryWrapperBuilder(params)
                .orderByDesc(ScheduleJob::getId);

        return ApiResultResponse.success(scheduleJobService.page(pageable.page(), queryWrapper));
    }


    @GetMapping("detail/{id}")
    @ApiOperation("定时任务信息")
    @PreAuthorize("@pms.hasPermission('system:schedule:detail')")
    public ApiResultResponse<ScheduleJob> info(@PathVariable("id") Long id) {
        return ApiResultResponse.success(scheduleJobService.getById(id));
    }


    @PostMapping
    @ApiOperation("保存定时任务")
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
    @ApiOperation("修改定时任务")
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
    @ApiOperation("删除定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:delete')")
    public ApiResultResponse<Object> delete(@RequestBody Long[] jobIds) {
        scheduleJobService.deleteBatch(jobIds);
        return ApiResultResponse.success();
    }

    @PostMapping("run")
    @ApiOperation("立即执行任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:run')")
    public ApiResultResponse<Object> run(@RequestBody Long[] jobIds) {
        scheduleJobService.run(jobIds);
        return ApiResultResponse.success();
    }


    @PostMapping("pause")
    @ApiOperation("暂停定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:pause')")
    public ApiResultResponse<Object> pause(@RequestBody Long[] jobIds) {
        scheduleJobService.pause(jobIds);
        return ApiResultResponse.success();
    }


    @PostMapping("resume")
    @ApiOperation("恢复定时任务")
    @PreAuthorize("@pms.hasPermission('system:schedule:resume')")
    public ApiResultResponse<Object> resume(@RequestBody Long[] jobIds) {
        scheduleJobService.resume(jobIds);
        return ApiResultResponse.success();
    }

}
