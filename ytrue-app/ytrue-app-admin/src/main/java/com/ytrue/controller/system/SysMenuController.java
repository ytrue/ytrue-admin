package com.ytrue.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytrue.infra.core.response.ResponseCodeEnum;

import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.bean.dataobject.system.SysMenu;
import com.ytrue.bean.query.system.SysMenuQuery;
import com.ytrue.service.system.SysMenuService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.util.QueryHelp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Tag(name = "菜单管理")
@RequestMapping("/sys/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:menu:list')")
    public ServerResponseEntity<List<SysMenu>> list(SysMenuQuery params) {

        LambdaQueryWrapper<SysMenu> queryWrapper = QueryHelp.<SysMenu>lambdaQueryWrapperBuilder(params)
                .orderByAsc(SysMenu::getMenuSort)
                .orderByDesc(SysMenu::getId);

        return ServerResponseEntity.success(sysMenuService.list(queryWrapper));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:menu:detail')")
    public ServerResponseEntity<SysMenu> detail(@PathVariable("id") Long id) {
        SysMenu data = sysMenuService.getById(id);
        AssertUtil.notNull(data, ResponseCodeEnum.DATA_NOT_FOUND);
        return ServerResponseEntity.success(data);
    }

    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:menu:add')")
    public ServerResponseEntity<Object> add(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.addMenu(sysMenu);
        return ServerResponseEntity.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:menu:update')")
    public ServerResponseEntity<Object> update(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.updateMenu(sysMenu);
        return ServerResponseEntity.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:menu:delete')")
    public ServerResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        sysMenuService.removeBatchMenu(ids);
        return ServerResponseEntity.success();
    }
}
