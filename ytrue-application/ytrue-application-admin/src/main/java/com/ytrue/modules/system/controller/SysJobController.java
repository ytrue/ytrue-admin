package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.SysJob;
import com.ytrue.modules.system.service.ISysJobService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @SysLog
    @PostMapping("page")
    @ApiOperation("分页")
    public ApiResultResponse<IPage<SysJob>> page(@RequestBody(required = false) PageQueryEntity<SysJob> pageQueryEntity) {
        IPage<SysJob> page = sysJobService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @SysLog
    @GetMapping("list")
    @ApiOperation("列表")
    public ApiResultResponse<List<SysJob>> list() {
        return ApiResultResponse.success(sysJobService.list());
    }

    @SysLog
    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysJob> detail(@PathVariable("id") Long id) {
        SysJob data = sysJobService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysJob sysJob) {
        SysJob job = sysJobService.lambdaQuery().eq(SysJob::getName, sysJob.getName()).one();
        AssertUtils.isNull(job, ResponseCode.JOB_EXISTS);
        sysJobService.save(sysJob);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysJob sysJob) {
        SysJob job = sysJobService
                .lambdaQuery()
                .eq(SysJob::getName, sysJob.getName())
                .ne(SysJob::getId, sysJob.getId())
                .one();
        AssertUtils.isNull(job, ResponseCode.JOB_EXISTS);
        sysJobService.updateById(sysJob);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysJobService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }
}
