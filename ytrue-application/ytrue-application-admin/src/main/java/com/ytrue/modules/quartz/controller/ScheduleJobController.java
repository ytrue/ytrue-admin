package com.ytrue.modules.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.quartz.model.ScheduleJob;
import com.ytrue.modules.quartz.service.ScheduleJobService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ScheduleJobService scheduleJobService;


    @PostMapping("/page")
    @ApiOperation("分页查询数据")
    public ApiResultResponse<IPage<ScheduleJob>> page(@RequestBody(required = false) PageQueryEntity<ScheduleJob> pageQueryEntity) {

        IPage<ScheduleJob> page = scheduleJobService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }


    @GetMapping("/{jobId}/info")
    @ApiOperation("定时任务信息")
    public ApiResultResponse<ScheduleJob> info(@PathVariable("jobId") Long jobId) {
        return ApiResultResponse.success(scheduleJobService.getById(jobId));
    }


    @PostMapping
    @ApiOperation("保存定时任务")
    public ApiResultResponse<Object> save(@Validated @RequestBody ScheduleJob scheduleJob) {


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
    public ApiResultResponse<Object> delete(@RequestBody Long[] jobIds) {
        scheduleJobService.deleteBatch(jobIds);
        return ApiResultResponse.success();
    }

    @PostMapping("/run")
    @ApiOperation("立即执行任务")
    public ApiResultResponse<Object> run(@RequestBody Long[] jobIds) {
        scheduleJobService.run(jobIds);
        return ApiResultResponse.success();
    }


    @PostMapping("/pause")
    @ApiOperation("暂停定时任务")
    public ApiResultResponse<Object> pause(@RequestBody Long[] jobIds) {
        scheduleJobService.pause(jobIds);
        return ApiResultResponse.success();
    }


    @PostMapping("/resume")
    @ApiOperation("恢复定时任务")
    public ApiResultResponse<Object> resume(@RequestBody Long[] jobIds) {
        scheduleJobService.resume(jobIds);
        return ApiResultResponse.success();
    }

}
