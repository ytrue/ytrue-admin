package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.dto.params.SysDeptSearchParams;
import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.service.ISysDeptService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysDeptController
 * @date 2022/12/7 11:47
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("sys/dept")
@AllArgsConstructor
public class SysDeptController {

    private final ISysDeptService sysDeptService;

    @GetMapping("list")
    @ApiOperation("列表")
    @PreAuthorize("@pms.hasPermission('system:dept:list')")
    public ApiResultResponse<List<SysDept>> list(SysDeptSearchParams params) {

        LambdaQueryWrapper<SysDept> queryWrapper = QueryHelp.<SysDept>lambdaQueryWrapperBuilder(params)
                .orderByAsc(SysDept::getDeptSort)
                .orderByDesc(SysDept::getId);

        return ApiResultResponse.success(sysDeptService.list(queryWrapper));
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    @PreAuthorize("@pms.hasPermission('system:dept:detail')")
    public ApiResultResponse<SysDept> detail(@PathVariable("id") Long id) {
        SysDept data = sysDeptService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("@pms.hasPermission('system:dept:add')")
    public ApiResultResponse<Object> add(@Valid @RequestBody SysDept sysDept) {
        sysDeptService.addDept(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("@pms.hasPermission('system:dept:update')")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysDept sysDept) {
        sysDeptService.updateDept(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("@pms.hasPermission('system:dept:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysDeptService.removeBatchDept(ids);
        return ApiResultResponse.success();
    }

}
