package com.ytrue.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytrue.bean.dataobject.system.SysDept;
import com.ytrue.bean.query.system.SysDeptListQuery;
import com.ytrue.bean.req.system.SysDeptAddReq;
import com.ytrue.bean.req.system.SysDeptUpdateReq;
import com.ytrue.infra.core.response.ServerResponseEntity;
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
    public ServerResponseEntity<List<SysDept>> list(SysDeptListQuery queryParam) {
        // 数据范围限制
        Set<Long> deptIds = sysDeptService.listCurrentAccountDeptId();
        LambdaQueryWrapper<SysDept> queryWrapper = QueryHelp.<SysDept>lambdaQueryWrapperBuilder(queryParam)
                .in(CollectionUtil.isNotEmpty(deptIds), SysDept::getId, deptIds)
                .orderByAsc(SysDept::getDeptSort)
                .orderByDesc(SysDept::getId);

        return ServerResponseEntity.success(sysDeptService.list(queryWrapper));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:dept:detail')")
    public ServerResponseEntity<SysDept> getSysDeptById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysDeptService.getSysDeptById(id));
    }

    @SysLog
    @PostMapping
    @Operation(summary = "新增")
    @PreAuthorize("@pms.hasPermission('system:dept:add')")
    public ServerResponseEntity<Void> addSysDept(@Validated @RequestBody SysDeptAddReq requestParam) {
        sysDeptService.addSysDept(requestParam);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:dept:update')")
    public ServerResponseEntity<Void> updateSysDept(@Validated @RequestBody SysDeptUpdateReq requestParam) {
        sysDeptService.updateSysDept(requestParam);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:dept:delete')")
    public ServerResponseEntity<Void> removeBatchSysDeptByIds(@RequestBody List<Long> ids) {
        sysDeptService.removeBatchSysDeptByIds(ids);
        return ServerResponseEntity.success();
    }

}
