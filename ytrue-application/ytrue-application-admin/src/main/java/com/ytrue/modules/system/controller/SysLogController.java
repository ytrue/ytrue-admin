package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.po.SysLog;
import com.ytrue.modules.system.service.ISysLogService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ytrue
 * @description: SysLogController
 * @date 2022/12/15 15:38
 */
@Api(tags = "日志管理")
@RestController
@RequestMapping("/sys/log")
@AllArgsConstructor
public class SysLogController {

    private final ISysLogService sysLogService;


    @PostMapping("page")
    @ApiOperation("分页")
    @PreAuthorize("@pms.hasPermission('system:log:page')")
    public ApiResultResponse<IPage<SysLog>> page(@RequestBody(required = false) PageQueryEntity pageQueryEntity) {
        IPage<SysLog> page = sysLogService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("@pms.hasPermission('system:log:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysLogService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }

    @DeleteMapping("clear")
    @ApiOperation("清空")
    @PreAuthorize("@pms.hasPermission('system:log:clear')")
    public ApiResultResponse<Object> clear() {
        sysLogService.remove(Wrappers.emptyWrapper());
        return ApiResultResponse.success();
    }
}
