package com.ytrue.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.dataobject.system.SysJob;
import com.ytrue.bean.query.system.SysJobQuery;
import com.ytrue.infra.core.response.ResponseCodeEnum;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.db.entity.Pageable;
import com.ytrue.service.system.SysJobService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.util.QueryHelp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ytrue
 * @description: SysJobController
 * @date 2022/12/7 10:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "岗位管理")
@RequestMapping("/sys/job")
public class SysJobController {

    private final SysJobService sysJobService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:job:page')")
    public ServerResponseEntity<IPage<SysJob>> page(SysJobQuery params, Pageable pageable) {

        LambdaQueryWrapper<SysJob> queryWrapper = QueryHelp.<SysJob>lambdaQueryWrapperBuilder(params)
                .orderByAsc(SysJob::getJobSort)
                .orderByDesc(SysJob::getId);

        return ServerResponseEntity.success(sysJobService.page(pageable.page(), queryWrapper));
    }


    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:job:list')")
    public ServerResponseEntity<List<SysJob>> list() {
        return ServerResponseEntity.success(
                sysJobService
                        .lambdaQuery()
                        .orderByAsc(SysJob::getJobSort)
                        .orderByDesc(SysJob::getId)
                        .list()
        );
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:job:detail')")
    public ServerResponseEntity<SysJob> detail(@PathVariable("id") Long id) {
        SysJob data = sysJobService.getById(id);
        AssertUtil.notNull(data, ResponseCodeEnum.DATA_NOT_FOUND);
        return ServerResponseEntity.success(data);
    }


    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:job:add')")
    public ServerResponseEntity<Void> add(@Validated @RequestBody SysJob sysJob) {
        SysJob job = sysJobService.lambdaQuery().eq(SysJob::getJobName, sysJob.getJobName()).one();
        AssertUtil.isNull(job, ServerResponseCode.error("岗位已存在"));
        sysJobService.save(sysJob);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:job:update')")
    public ServerResponseEntity<Void> update(@Validated @RequestBody SysJob sysJob) {
        SysJob job = sysJobService
                .lambdaQuery()
                .eq(SysJob::getJobName, sysJob.getJobName())
                .ne(SysJob::getId, sysJob.getId())
                .one();
        AssertUtil.isNull(job, ServerResponseCode.error("岗位已存在"));
        sysJobService.updateById(sysJob);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:job:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids) {
        // 需要校验用户与岗位得绑定关系
        sysJobService.removeBatchByIds(ids);
        return ServerResponseEntity.success();
    }
}
