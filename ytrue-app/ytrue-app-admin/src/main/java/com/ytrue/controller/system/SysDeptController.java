package com.ytrue.controller.system;

import com.ytrue.bean.query.system.SysDeptListQuery;
import com.ytrue.bean.req.system.SysDeptAddReq;
import com.ytrue.bean.req.system.SysDeptUpdateReq;
import com.ytrue.bean.resp.system.SysDeptIdResp;
import com.ytrue.bean.resp.system.SysDeptListResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.service.system.SysDeptService;
import com.ytrue.tools.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ServerResponseEntity<List<SysDeptListResp>> listBySysDeptListQuery(SysDeptListQuery queryParam) {
        return ServerResponseEntity.success(sysDeptService.listBySysDeptListQuery(queryParam));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:dept:detail')")
    public ServerResponseEntity<SysDeptIdResp> getBySysDeptId(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysDeptService.getBySysDeptId(id));
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
