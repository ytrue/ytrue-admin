package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.modules.system.model.po.SysRole;
import com.ytrue.modules.system.model.dto.SysRoleDTO;
import com.ytrue.modules.system.service.ISysRoleService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.PageQueryEntity;
import com.ytrue.tools.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ytrue
 * @description: SysRoleController
 * @date 2022/12/7 15:24
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "角色管理")
@RequestMapping("/sys/role")
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @PostMapping("page")
    @ApiOperation("分页查询")
    public ApiResultResponse<IPage<SysRole>> page(@RequestBody(required = false) PageQueryEntity<SysRole> pageQueryEntity) {
        // 这里还要做数据范围过滤 TODO
        IPage<SysRole> page = sysRoleService.paginate(pageQueryEntity);
        return ApiResultResponse.success(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public ApiResultResponse<List<SysRole>> list() {
        return ApiResultResponse.success(sysRoleService.list());
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysRoleDTO> detail(@PathVariable("id") Long id) {
        return ApiResultResponse.success(sysRoleService.getRoleById(id));
    }

    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysRoleDTO sysRoleDTO) {
        sysRoleService.addRole(sysRoleDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysRoleDTO sysRoleDTO) {
        sysRoleService.updateRole(sysRoleDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysRoleService.removeBatchRole(ids);
        return ApiResultResponse.success();
    }

}
