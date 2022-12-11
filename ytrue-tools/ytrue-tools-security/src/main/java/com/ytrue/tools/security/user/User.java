package com.ytrue.tools.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/5/10 15:53
 * @description User
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;

    private String username;

    private String password;

    private Set<String> authorities;

    private Map<String, String> extend;
}
