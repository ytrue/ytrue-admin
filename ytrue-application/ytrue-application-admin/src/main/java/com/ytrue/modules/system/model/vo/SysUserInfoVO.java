package com.ytrue.modules.system.model.vo;

import com.ytrue.modules.system.model.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserInfoVO
 * @date 2022/12/8 15:53
 */
@Data
public class SysUserInfoVO {

    private SysUser user;

    private Set<String> roles;

    private Set<String> permissions;
}
