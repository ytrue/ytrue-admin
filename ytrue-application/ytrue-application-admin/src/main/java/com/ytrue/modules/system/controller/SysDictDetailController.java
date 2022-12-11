package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.model.SysDictDetail;
import com.ytrue.modules.system.service.ISysDictDetailService;
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
 * @description: SysDictDetailController
 * @date 2022/12/7 9:14
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "字典详情管理")
@RequestMapping("/sys/dictDetail")
public class SysDictDetailController {

    private final ISysDictDetailService sysDictDetailService;

    @SysLog
    @PostMapping("page")
    @ApiOperation("分页")
    public ApiResultResponse<IPage<SysDictDetail>> page(@RequestBody(required = false) PageQueryEntity<SysDictDetail> pageQueryEntity) {
        IPage<SysDictDetail> page = sysDictDetailService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @SysLog
    @GetMapping("list")
    @ApiOperation("列表")
    public ApiResultResponse<List<SysDictDetail>> list(@RequestBody(required = false) QueryEntity<SysDictDetail> queryEntity) {
        return ApiResultResponse.success(sysDictDetailService.list(queryEntity));
    }

    @SysLog
    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysDictDetail> detail(@PathVariable("id") Long id) {
        SysDictDetail data = sysDictDetailService.getById(id);
        AssertUtils.notNull(data, ResponseCode.DATA_NOT_FOUND);
        return ApiResultResponse.success(data);
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysDictDetail sysDictDetail) {
        sysDictDetailService.save(sysDictDetail);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysDictDetail sysDictDetail) {
        sysDictDetailService.updateById(sysDictDetail);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysDictDetailService.removeBatchByIds(ids);
        return ApiResultResponse.success();
    }
}
