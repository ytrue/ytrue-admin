package com.ytrue.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.db.mybatis.entity.Pageable;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.req.SysRoleReq;
import com.ytrue.modules.system.model.query.SysRoleQuery;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.service.ISysRoleService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleController
 * @date 2022/12/7 15:24
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "角色管理")
@RequestMapping("/sys/role")
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @GetMapping("page")
    @Operation(summary="分页查询")
    @PreAuthorize("@pms.hasPermission('system:role:page')")
    public ApiResultResponse<IPage<SysRole>> page(SysRoleQuery params, Pageable pageable) {
        // 数据范围限制
        Set<Long> roleIds = sysRoleService.getRoleIdsByDataScope();
        LambdaQueryWrapper<SysRole> queryWrapper = QueryHelp.<SysRole>lambdaQueryWrapperBuilder(params).in(CollectionUtil.isNotEmpty(roleIds), SysRole::getId, roleIds).orderByAsc(SysRole::getRoleSort).orderByDesc(SysRole::getId);

        return ApiResultResponse.success(sysRoleService.page(pageable.page(), queryWrapper));
    }

    @GetMapping("list")
    @Operation(summary="列表")
    @PreAuthorize("@pms.hasPermission('system:role:list')")
    public ApiResultResponse<List<SysRole>> list() {
        // 数据范围限制
        Set<Long> roleIds = sysRoleService.getRoleIdsByDataScope();
        LambdaQueryWrapper<SysRole> query = Wrappers.<SysRole>lambdaQuery().in(CollectionUtil.isNotEmpty(roleIds), SysRole::getId, roleIds);
        List<SysRole> roles = sysRoleService.list(query);
        return ApiResultResponse.success(roles);
    }

    @GetMapping("detail/{id}")
    @Operation(summary="详情")
    @PreAuthorize("@pms.hasPermission('system:role:detail')")
    public ApiResultResponse<SysRoleReq> detail(@PathVariable("id") Long id) {
        return ApiResultResponse.success(sysRoleService.getRoleById(id));
    }

    @SysLog
    @PostMapping
    @Operation(summary="保存")
    @PreAuthorize("@pms.hasPermission('system:role:add')")
    public ApiResultResponse<Object> add(@Validated @RequestBody SysRoleReq sysRoleReq) {
        sysRoleService.addRole(sysRoleReq);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary="修改")
    @PreAuthorize("@pms.hasPermission('system:role:update')")
    public ApiResultResponse<Object> update(@Validated @RequestBody SysRoleReq sysRoleReq) {
        sysRoleService.updateRole(sysRoleReq);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary="删除")
    @PreAuthorize("@pms.hasPermission('system:role:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysRoleService.removeBatchRole(ids);
        return ApiResultResponse.success();
    }

}
