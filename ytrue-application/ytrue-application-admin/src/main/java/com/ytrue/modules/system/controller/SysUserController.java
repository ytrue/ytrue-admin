package com.ytrue.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.BeanUtils;
import com.ytrue.modules.system.model.po.SysDept;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.model.dto.SysUserDTO;
import com.ytrue.modules.system.model.vo.SysUserListVO;
import com.ytrue.modules.system.service.ISysDeptService;
import com.ytrue.modules.system.service.ISysUserService;
import com.ytrue.tools.log.annotation.SysLog;
import com.ytrue.tools.query.entity.PageQueryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    private final ISysDeptService sysDeptService;

    @PostMapping("page")
    @ApiOperation("分页")
    public ApiResultResponse<IPage<SysUserListVO>> page(@RequestBody(required = false) PageQueryEntity<SysUser> pageQueryEntity) {
        // 这里就不去联表查询了，后期待优化
        IPage<SysUserListVO> page = sysUserService.paginate(pageQueryEntity).convert(sysUser -> {
            SysUserListVO sysUserListVO = BeanUtils.cgLibCopyBean(sysUser, SysUserListVO::new);
            sysUserListVO.setDeptName(Optional.ofNullable(sysDeptService.getById(sysUser.getDeptId())).map(SysDept::getDeptName).orElse(null));
            return sysUserListVO;
        });
        return ApiResultResponse.success(page);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")
    public ApiResultResponse<SysUserDTO> detail(@PathVariable("id") Long id) {
        return ApiResultResponse.success(sysUserService.getUserById(id));
    }


    @SysLog
    @PostMapping
    @ApiOperation("保存")
    public ApiResultResponse<Object> save(@Valid @RequestBody SysUserDTO sysUserDTO) {
        sysUserService.addUser(sysUserDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @PutMapping
    @ApiOperation("修改")
    public ApiResultResponse<Object> update(@Valid @RequestBody SysUserDTO sysUserDTO) {
        sysUserService.updateUser(sysUserDTO);
        return ApiResultResponse.success();
    }

    @SysLog
    @DeleteMapping
    @ApiOperation("删除")
    public ApiResultResponse<Object> delete(@RequestBody List<Long> ids) {
        sysUserService.removeBatchUser(ids);
        return ApiResultResponse.success();
    }
}
