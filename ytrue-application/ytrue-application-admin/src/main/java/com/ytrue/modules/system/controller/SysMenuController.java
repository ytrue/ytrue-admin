package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.query.SysMenuQuery;
import com.ytrue.modules.system.model.po.SysMenu;
import com.ytrue.modules.system.service.ISysMenuService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.util.QueryHelp;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
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

    private final ISysMenuService sysMenuService;

    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("@pms.hasPermission('system:menu:list')")
    public ApiResultResponse<List<SysMenu>> list(SysMenuQuery params) {

        LambdaQueryWrapper<SysMenu> queryWrapper = QueryHelp.<SysMenu>lambdaQueryWrapperBuilder(params)
                .orderByAsc(SysMenu::getMenuSort)
                .orderByDesc(SysMenu::getId);

        return ApiResultResponse.success(sysMenuService.list(queryWrapper));
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    @PreAuthorize("@pms.hasPermission('system:menu:detail')")
    public ApiResultResponse<SysMenu> detail(@PathVariable("id") Long id) {
        SysMenu data = sysMenuService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("@pms.hasPermission('system:menu:add')")
    public ApiResultResponse<Object> add(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.addMenu(sysMenu);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("@pms.hasPermission('system:menu:update')")
    public ApiResultResponse<Object> update(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.updateMenu(sysMenu);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:menu:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysMenuService.removeBatchMenu(ids);
        return ApiResultResponse.success();
    }
}
