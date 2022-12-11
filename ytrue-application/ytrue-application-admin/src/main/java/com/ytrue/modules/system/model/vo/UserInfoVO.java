package com.ytrue.modules.system.model.vo;

import com.ytrue.modules.system.model.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author ytrue
 * @description: UserInfoVO
 * @date 2022/12/8 15:53
 */
@Data
@Accessors(chain = true)
public class UserInfoVO {

    private SysUser user;

    private Set<String> roles;

    private Set<String> permissions;
}
