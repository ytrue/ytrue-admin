package com.ytrue.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.query.system.SysDeptQuery;
import com.ytrue.infra.core.response.ResponseCodeEnum;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.service.system.SysDeptService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.util.QueryHelp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService sysDeptService;


    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:dept:list')")
    public ServerResponseEntity<List<SysDept>> list(SysDeptQuery params) {
        // 数据范围限制
        Set<Long> deptIds = sysDeptService.listCurrentAccountDeptId();
        LambdaQueryWrapper<SysDept> queryWrapper = QueryHelp.<SysDept>lambdaQueryWrapperBuilder(params)
                .in(CollectionUtil.isNotEmpty(deptIds), SysDept::getId, deptIds)
                .orderByAsc(SysDept::getDeptSort)
                .orderByDesc(SysDept::getId);

        return ServerResponseEntity.success(sysDeptService.list(queryWrapper));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:dept:detail')")
    public ServerResponseEntity<SysDept> detail(@PathVariable("id") Long id) {
        SysDept data = sysDeptService.getById(id);
        AssertUtil.notNull(data, ResponseCodeEnum.DATA_NOT_FOUND);
        return ServerResponseEntity.success(data);
    }

    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:dept:add')")
    public ServerResponseEntity<Void> add(@Validated @RequestBody SysDept sysDept) {
        sysDeptService.addDept(sysDept);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:dept:update')")
    public ServerResponseEntity<Void> update(@Validated @RequestBody SysDept sysDept) {
        sysDeptService.updateDept(sysDept);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:dept:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids) {
        sysDeptService.removeBatchDept(ids);
        return ServerResponseEntity.success();
    }

}
