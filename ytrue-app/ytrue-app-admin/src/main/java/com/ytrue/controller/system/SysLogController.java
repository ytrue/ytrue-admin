package com.ytrue.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysLog;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.service.system.SysLogService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ytrue
 * @description: SysLogController
 * @date 2022/12/15 15:38
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class SysLogController {

    private final SysLogService sysLogService;


    @PostMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:log:page')")
    public ServerResponseEntity<IPage<SysLog>> page(@RequestBody(required = false) PageQueryEntity pageQueryEntity) {
        IPage<SysLog> page = sysLogService.paginate(pageQueryEntity);
        return ServerResponseEntity.success(page);
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("@pms.hasPermission('system:log:delete')")
    public ServerResponseEntity<Void> removeBatchSysLogByIds(@RequestBody List<Long> ids) {
        sysLogService.removeBatchByIds(ids);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("clear")
    @Operation(summary = "清空")
    @PreAuthorize("@pms.hasPermission('system:log:clear')")
    public ServerResponseEntity<Void> removeAll() {
        sysLogService.remove(Wrappers.emptyWrapper());
        return ServerResponseEntity.success();
    }
}
