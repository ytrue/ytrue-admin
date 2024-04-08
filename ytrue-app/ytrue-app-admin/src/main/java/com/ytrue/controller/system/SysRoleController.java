package com.ytrue.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysRole;
import com.ytrue.bean.query.system.SysRolePageQuery;
import com.ytrue.bean.req.system.SysRoleAddReq;
import com.ytrue.bean.req.system.SysRoleUpdateReq;
import com.ytrue.bean.resp.system.SysRoleDetailResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.service.system.SysRoleService;
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
import java.util.Set;

/**
 * @author ytrue
 * @description: SysRoleController
 * @date 2022/12/7 15:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "角色管理")
@RequestMapping("/sys/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@pms.hasPermission('system:role:page')")
    public ServerResponseEntity<IPage<SysRole>> page(SysRolePageQuery queryParam) {
        // 数据范围限制
        Set<Long> roleIds = sysRoleService.listCurrentAccountRoleId();
        LambdaQueryWrapper<SysRole> queryWrapper = QueryHelp.<SysRole>lambdaQueryWrapperBuilder(queryParam)
                .in(CollectionUtil.isNotEmpty(roleIds), SysRole::getId, roleIds)
                .orderByAsc(SysRole::getRoleSort)
                .orderByDesc(SysRole::getId);

        return ServerResponseEntity.success(sysRoleService.page(queryParam.page(), queryWrapper));
    }

    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:role:list')")
    public ServerResponseEntity<List<SysRole>> list() {
        // 数据范围限制
        Set<Long> roleIds = sysRoleService.listCurrentAccountRoleId();
        LambdaQueryWrapper<SysRole> query = Wrappers.<SysRole>lambdaQuery()
                .in(CollectionUtil.isNotEmpty(roleIds), SysRole::getId, roleIds);

        List<SysRole> roles = sysRoleService.list(query);
        return ServerResponseEntity.success(roles);
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:role:detail')")
    public ServerResponseEntity<SysRoleDetailResp> detail(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysRoleService.getRoleById(id));
    }

    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:role:add')")
    public ServerResponseEntity<Void> addSysRole(@Validated @RequestBody SysRoleAddReq requestParam) {
        sysRoleService.addSysRole(requestParam);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:role:update')")
    public ServerResponseEntity<Void> updateSysRole(@Validated @RequestBody SysRoleUpdateReq requestParam) {
        sysRoleService.updateSysRole(requestParam);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:role:delete')")
    public ServerResponseEntity<Void> removeBatchSysRoleByIds(@RequestBody List<Long> ids) {
        sysRoleService.removeBatchSysRoleByIds(ids);
        return ServerResponseEntity.success();
    }

}
