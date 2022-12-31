package com.ytrue.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.entity.Pageable;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.dto.SysRoleDTO;
import com.ytrue.modules.system.model.dto.params.SysRoleSearchParams;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.service.ISysRoleService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Api(tags = "角色管理")
@RequestMapping("/sys/role")
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @GetMapping("page")
    @ApiOperation("分页查询")
    @PreAuthorize("@pms.hasPermission('system:role:page')")
    public ApiResultResponse<IPage<SysRole>> page(SysRoleSearchParams params, Pageable pageable) {
        // 数据范围限制
        Set<Long> roleIds = sysRoleService.getRoleIdsByDataScope();
        LambdaQueryWrapper<SysRole> queryWrapper = QueryHelp.<SysRole>lambdaQueryWrapperBuilder(params)
                .in(CollectionUtil.isNotEmpty(roleIds), SysRole::getId, roleIds)
                .orderByAsc(SysRole::getRoleSort)
                .orderByDesc(SysRole::getId);

        return ApiResultResponse.success(sysRoleService.page(pageable.page(), queryWrapper));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @PreAuthorize("@pms.hasPermission('system:role:list')")
    public ApiResultResponse<List<SysRole>> list() {
        // 数据范围限制
        Set<Long> roleIds = sysRoleService.getRoleIdsByDataScope();
        LambdaQueryWrapper<SysRole> query = Wrappers.<SysRole>lambdaQuery().in(CollectionUtil.isNotEmpty(roleIds), SysRole::getId, roleIds);
        List<SysRole> roles = sysRoleService.list(query);
        return ApiResultResponse.success(roles);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    @PreAuthorize("@pms.hasPermission('system:role:detail')")
    public ApiResultResponse<SysRoleDTO> detail(@PathVariable("id") Long id) {
        return ApiResultResponse.success(sysRoleService.getRoleById(id));
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("@pms.hasPermission('system:role:add')")
    public ApiResultResponse<Object> add(@Valid @RequestBody SysRoleDTO sysRoleDTO) {
        sysRoleService.addRole(sysRoleDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("@pms.hasPermission('system:role:update')")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysRoleDTO sysRoleDTO) {
        sysRoleService.updateRole(sysRoleDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("@pms.hasPermission('system:role:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysRoleService.removeBatchRole(ids);
        return ApiResultResponse.success();
    }

}
