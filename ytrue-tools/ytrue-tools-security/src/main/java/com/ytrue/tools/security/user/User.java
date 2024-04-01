package com.ytrue.tools.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/5/10 15:53
 * @description User
 */
@Data

@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 凭证
     */
    private String password;

    /**
     * 权限
     */
    private Set<String> authorities;

    /**
     * 角色
     */
    private Set<String> roles;

    /**
     * 自定义扩展信息
     */
    private Map<String, String> extend;
}
