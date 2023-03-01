package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.entity.Pageable;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.dto.params.SysJobSearchParams;
import com.ytrue.modules.system.model.po.SysJob;
import com.ytrue.modules.system.service.ISysJobService;
import com.ytrue.modules.system.service.manager.DataScopeManager;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysJobController
 * @date 2022/12/7 10:57
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "岗位管理")
@RequestMapping("/sys/job")
public class SysJobController {

    private final ISysJobService sysJobService;

    private final DataScopeManager dataScopeManager;

    @GetMapping("page")
    @ApiOperation("分页")
    @PreAuthorize("@pms.hasPermission('system:job:page')")
    public ApiResultResponse<IPage<SysJob>> page(SysJobSearchParams params, Pageable pageable) {

        LambdaQueryWrapper<SysJob> queryWrapper = QueryHelp.<SysJob>lambdaQueryWrapperBuilder(params)
                .orderByAsc(SysJob::getJobSort)
                .orderByDesc(SysJob::getId);

        return ApiResultResponse.success(sysJobService.page(pageable.page(), queryWrapper));
    }


    @GetMapping("list")
    @ApiOperation("列表")
    @PreAuthorize("@pms.hasPermission('system:job:list')")
    public ApiResultResponse<List<SysJob>> list() {
        return ApiResultResponse.success(
                sysJobService
                        .lambdaQuery()
                        .orderByAsc(SysJob::getJobSort)
                        .orderByDesc(SysJob::getId)
                        .list()
        );
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    @PreAuthorize("@pms.hasPermission('system:job:detail')")
    public ApiResultResponse<SysJob> detail(@PathVariable("id") Long id) {
        SysJob data = sysJobService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("@pms.hasPermission('system:job:add')")
    public ApiResultResponse<Object> add(@Validated @RequestBody SysJob sysJob) {
        SysJob job = sysJobService.lambdaQuery().eq(SysJob::getJobName, sysJob.getJobName()).one();
        AssertUtils.isNull(job, ResponseCode.JOB_EXISTS);
        sysJobService.save(sysJob);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("@pms.hasPermission('system:job:update')")
    public ApiResultResponse<Object> update(@Validated  @RequestBody SysJob sysJob) {
        SysJob job = sysJobService
                .lambdaQuery()
                .eq(SysJob::getJobName, sysJob.getJobName())
                .ne(SysJob::getId, sysJob.getId())
                .one();
        AssertUtils.isNull(job, ResponseCode.JOB_EXISTS);
        sysJobService.updateById(sysJob);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("@pms.hasPermission('system:job:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        // 需要校验用户与岗位得绑定关系
        sysJobService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }
}
