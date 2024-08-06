package com.ytrue.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.bean.query.system.SysJobPageQuery;
import com.ytrue.bean.req.system.SysJobAddReq;
import com.ytrue.bean.req.system.SysJobUpdateReq;
import com.ytrue.bean.resp.system.SysJobIdResp;
import com.ytrue.bean.resp.system.SysJobListResp;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.log.annotation.OperateLog;
import com.ytrue.service.system.SysJobService;
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
 * @description: SysJobController
 * @date 2022/12/7 10:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "岗位管理")
@RequestMapping("/sys/job")
public class SysJobController {

    private final SysJobService sysJobService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("@pms.hasPermission('system:job:page1')")
    public ServerResponseEntity<IPage<SysJobListResp>> listBySysJobPageQuery(SysJobPageQuery queryParam) {
        return ServerResponseEntity.success(sysJobService.listBySysJobPageQuery(queryParam));
    }

    @GetMapping("list")
    @Operation(summary = "列表")
    // @PreAuthorize("@pms.hasPermission('system:job:list')")
    public ServerResponseEntity<List<SysJobListResp>> listAll() {

        SysJobPageQuery sysJobPageQuery = SysJobPageQuery.builder().status(Boolean.TRUE).build();
        sysJobPageQuery.setPageIndex(1);
        sysJobPageQuery.setPageSize(Integer.MAX_VALUE);

        List<SysJobListResp> list = sysJobService.listBySysJobPageQuery(sysJobPageQuery).getRecords();
        return ServerResponseEntity.success(list);
    }

    @GetMapping("detail/{id}")
    @Operation(summary = "详情")
    // @PreAuthorize("@pms.hasPermission('system:job:detail')")
    public ServerResponseEntity<SysJobIdResp> getBySysJobId(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(sysJobService.getBySysJobId(id));
    }


    @OperateLog
    @PostMapping
    @Operation(summary = "保存")
    // @PreAuthorize("@pms.hasPermission('system:job:add')")
    public ServerResponseEntity<Void> addSysJob(@Validated @RequestBody SysJobAddReq requestParam) {
        sysJobService.addSysJob(requestParam);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @PutMapping
    @Operation(summary = "修改")
    // @PreAuthorize("@pms.hasPermission('system:job:update')")
    public ServerResponseEntity<Void> updateSysJob(@Validated @RequestBody SysJobUpdateReq requestParam) {
        sysJobService.updateSysJob(requestParam);
        return ServerResponseEntity.success();
    }

    @OperateLog
    @DeleteMapping
    @Operation(summary = "删除")
    // @PreAuthorize("@pms.hasPermission('system:job:delete')")
    public ServerResponseEntity<Void> removeBySysJobIds(@RequestBody List<Long> ids) {
        // 需要校验用户与岗位得绑定关系
        sysJobService.removeBySysJobIds(ids);
        return ServerResponseEntity.success();
    }
}
