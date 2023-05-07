package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.po.SysLog;
import com.ytrue.modules.system.service.ISysLogService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ytrue
 * @description: SysLogController
 * @date 2022/12/15 15:38
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/sys/log")
@AllArgsConstructor
public class SysLogController {

    private final ISysLogService sysLogService;


    @PostMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:log:page')")
    public ApiResultResponse<IPage<SysLog>> page(@RequestBody(required = false) PageQueryEntity pageQueryEntity) {
        IPage<SysLog> page = sysLogService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:log:delete')")
    public ApiResultResponse<Object> delete(@RequestBody long[] ids) {
        sysLogService.removeBatchByIds(Collections.singletonList(ids));
        return ApiResultResponse.success();
    }

    @DeleteMapping("clear")
    @Operation(summary = "清空")
    @PreAuthorize("@pms.hasPermission('system:log:clear')")
    public ApiResultResponse<Object> clear() {
        sysLogService.remove(Wrappers.emptyWrapper());
        return ApiResultResponse.success();
    }
}
