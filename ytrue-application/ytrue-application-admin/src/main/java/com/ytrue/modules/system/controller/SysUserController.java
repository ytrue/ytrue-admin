package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.entity.Pageable;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.dto.params.SysUserSearchParams;
import com.ytrue.modules.system.model.dto.SysUserDTO;
import com.ytrue.modules.system.model.vo.SysUserListVO;
import com.ytrue.modules.system.service.ISysUserService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.utils.QueryHelp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysUserController
 * @date 2022/12/7 16:59
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("sys/user")
@AllArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    @GetMapping("page")
    @ApiOperation("分页")
    @PreAuthorize("@pms.hasPermission('system:user:page')")
    public ApiResultResponse<IPage<SysUserListVO>> page(SysUserSearchParams params, Pageable pageable) {

        QueryEntity queryEntity = QueryHelp
                .queryEntityBuilder(params)
                .addSort(SysUserListVO::getId, false);

        return ApiResultResponse.success(sysUserService.paginate(pageable.page(), queryEntity));
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    @PreAuthorize("@pms.hasPermission('system:user:detail')")
    public ApiResultResponse<SysUserDTO> detail(@PathVariable("id") Long id) {
        return ApiResultResponse.success(sysUserService.getUserById(id));
    }


    @SysLog
    @PostMapping
    @ApiOperation("保存")
    @PreAuthorize("@pms.hasPermission('system:user:add')")
    public ApiResultResponse<Object> add(@Valid @RequestBody SysUserDTO sysUserDTO) {
        sysUserService.addUser(sysUserDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    @PreAuthorize("@pms.hasPermission('system:user:update')")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysUserDTO sysUserDTO) {
        sysUserService.updateUser(sysUserDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    @PreAuthorize("@pms.hasPermission('system:user:delete')")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysUserService.removeBatchUser(ids);
        return ApiResultResponse.success();
    }
}
