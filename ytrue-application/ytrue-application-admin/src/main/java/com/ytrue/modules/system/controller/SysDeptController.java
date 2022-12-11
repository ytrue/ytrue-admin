package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.SysDept;
import com.ytrue.modules.system.service.ISysDeptService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysDeptController
 * @date 2022/12/7 11:47
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("sys/dept")
@AllArgsConstructor
public class SysDeptController {

    private final ISysDeptService sysDeptService;

    @SysLog
    @PostMapping("page")
    @ApiOperation("分页查询")
    public ApiResultResponse<IPage<SysDept>> page(@RequestBody(required = false) PageQueryEntity<SysDept> pageQueryEntity) {
        // 这里还要做数据范围过滤 TODO
        IPage<SysDept> page = sysDeptService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @SysLog
    @GetMapping("list")
    @ApiOperation("列表")
    public ApiResultResponse<List<SysDept>> list() {
        return ApiResultResponse.success(sysDeptService.list());
    }

    @SysLog
    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysDept> detail(@PathVariable("id") Long id) {
        SysDept data = sysDeptService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysDept sysDept) {
        sysDeptService.addDept(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysDept sysDept) {
        sysDeptService.updateDept(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysDeptService.removeBatchDept(ids);
        return ApiResultResponse.success();
    }

}
