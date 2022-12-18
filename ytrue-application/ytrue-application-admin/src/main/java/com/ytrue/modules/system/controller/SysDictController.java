package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.po.SysDict;
import com.ytrue.modules.system.service.ISysDictService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysDictController
 * @date 2022/12/7 9:14
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "字典管理")
@RequestMapping("/sys/dict")
public class SysDictController {

    private final ISysDictService sysDictService;

    @PostMapping("page")
    @ApiOperation("分页")
    public ApiResultResponse<IPage<SysDict>> page(@RequestBody(required = false) PageQueryEntity<SysDict> pageQueryEntity) {
        IPage<SysDict> page = sysDictService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public ApiResultResponse<List<SysDict>> list() {
        return ApiResultResponse.success(sysDictService.list());
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysDict> detail(@PathVariable("id") Long id) {
        SysDict data = sysDictService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysDict sysDept) {
        sysDictService.save(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysDict sysDept) {
        sysDictService.updateById(sysDept);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysDictService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }
}
