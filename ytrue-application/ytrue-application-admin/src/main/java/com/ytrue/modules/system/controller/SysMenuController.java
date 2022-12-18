package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.po.SysMenu;
import com.ytrue.modules.system.service.ISysMenuService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.PageQueryEntity;
import com.ytrue.tools.query.entity.QueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuController
 * @date 2022/12/7 14:12
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "菜单管理")
@RequestMapping("/sys/menu")
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @PostMapping("page")
    @ApiOperation("分页查询")
    public ApiResultResponse<IPage<SysMenu>> page(@RequestBody(required = false) PageQueryEntity<SysMenu> pageQueryEntity) {
        // 这里还要做数据范围过滤 TODO
        IPage<SysMenu> page = sysMenuService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public ApiResultResponse<List<SysMenu>> list(@RequestBody(required = false) QueryEntity<SysMenu> queryEntity) {
        return ApiResultResponse.success(sysMenuService.list(queryEntity));
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysMenu> detail(@PathVariable("id") Long id) {
        SysMenu data = sysMenuService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.addMenu(sysMenu);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.updateMenu(sysMenu);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysMenuService.removeBatchMenu(ids);
        return ApiResultResponse.success();
    }
}
