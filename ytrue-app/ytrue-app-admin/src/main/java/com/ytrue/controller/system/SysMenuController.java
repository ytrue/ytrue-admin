package com.ytrue.controller.system;

import com.ytrue.bean.dataobject.system.SysMenu;
import com.ytrue.bean.query.system.SysMenuListQuery;
import com.ytrue.bean.req.system.SysMenuAddReq;
import com.ytrue.bean.req.system.SysMenuUpdateReq;
import com.ytrue.bean.resp.system.SysMenuIdResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.service.system.SysMenuService;
import com.ytrue.tools.log.annotation.SysLog;
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
    public ServerResponseEntity<List<SysMenu>> list(SysMenuListQuery queryParam) {
        return ServerResponseEntity.success(sysMenuService.listBySysMenuListQuery(queryParam));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:menu:detail')")
    public ServerResponseEntity<SysMenuIdResp> detail(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysMenuService.getBySysMenuId(id));
    }

    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:menu:add')")
    public ServerResponseEntity<Void> add(@Validated @RequestBody SysMenuAddReq requestParam) {
        sysMenuService.addMenu(requestParam);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:menu:update')")
    public ServerResponseEntity<Void> update(@Validated @RequestBody SysMenuUpdateReq requestParam) {
        sysMenuService.updateMenu(requestParam);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:menu:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids) {
        sysMenuService.removeBatchMenu(ids);
        return ServerResponseEntity.success();
    }
}
