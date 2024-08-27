package com.ytrue.controller.system;

import com.ytrue.bean.dataobject.system.SysMenu;
import com.ytrue.bean.query.system.SysMenuListQuery;
import com.ytrue.bean.req.system.SysMenuAddReq;
import com.ytrue.bean.req.system.SysMenuUpdateReq;
import com.ytrue.bean.resp.system.SysMenuIdResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.log.annotation.OperateLog;
import com.ytrue.service.system.SysMenuService;
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
 * @description: SysMenuController
 * @date 2022/12/7 14:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "菜单管理")
@RequestMapping("/sys/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:menu:list')")
    public ServerResponseEntity<List<SysMenu>> listBySysMenuListQuery(SysMenuListQuery queryParam) {
        return ServerResponseEntity.success(sysMenuService.listBySysMenuListQuery(queryParam));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:menu:detail')")
    public ServerResponseEntity<SysMenuIdResp> getBySysMenuId(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysMenuService.getBySysMenuId(id));
    }

    @OperateLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:menu:add')")
    public ServerResponseEntity<Void> addSysMenu(@Validated @RequestBody SysMenuAddReq requestParam) {
        sysMenuService.addSysMenu(requestParam);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:menu:update')")
    public ServerResponseEntity<Void> updateSysMenu(@Validated @RequestBody SysMenuUpdateReq requestParam) {
        sysMenuService.updateSysMenu(requestParam);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:menu:delete')")
    public ServerResponseEntity<Void> removeBySysMenuIds(@RequestBody List<Long> ids) {
        sysMenuService.removeBySysMenuIds(ids);
        return ServerResponseEntity.success();
    }
}
