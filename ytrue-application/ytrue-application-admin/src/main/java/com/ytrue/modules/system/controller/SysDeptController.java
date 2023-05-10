package com.ytrue.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.query.SysDeptQuery;
import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.service.ISysDeptService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.utils.QueryHelp;
import com.ytrue.tools.security.annotation.IgnoreWebSecurity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysDeptController
 * @date 2022/12/7 11:47
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("sys/dept")
@AllArgsConstructor
public class SysDeptController {

    private final ISysDeptService sysDeptService;


    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:dept:list')")
    public ApiResultResponse<List<SysDept>> list(SysDeptQuery params) {
        // 数据范围限制
        Set<Long> deptIds = sysDeptService.getDeptIdByDataScope();
        LambdaQueryWrapper<SysDept> queryWrapper = QueryHelp.<SysDept>lambdaQueryWrapperBuilder(params)
                .in(CollectionUtil.isNotEmpty(deptIds), SysDept::getId, deptIds)
                .orderByAsc(SysDept::getDeptSort)
                .orderByDesc(SysDept::getId);

        return ApiResultResponse.success(sysDeptService.list(queryWrapper));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:dept:detail')")
    public ApiResultResponse<SysDept> detail(@PathVariable("id") Long id) {
        SysDept data = sysDeptService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:dept:add')")
    public ApiResultResponse<Object> add(@Validated @RequestBody SysDept sysDept) {
        sysDeptService.addDept(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:dept:update')")
    public ApiResultResponse<Object> update(@Validated @RequestBody SysDept sysDept) {
        sysDeptService.updateDept(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:dept:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysDeptService.removeBatchDept(ids);
        return ApiResultResponse.success();
    }

}
