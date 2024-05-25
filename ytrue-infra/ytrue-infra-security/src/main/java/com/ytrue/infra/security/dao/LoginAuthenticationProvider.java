package com.ytrue.infra.security.dao;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * @author ytrue
 * @date 2022/4/26 14:03
 * @description 自定义LoginAuthenticationProvider继承DaoAuthenticationProvider重写
 */
public class LoginAuthenticationProvider extends DaoAuthenticationProvider {


    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {

        Object credentials = authentication.getCredentials();
        if (Objects.isNull(credentials) || "".equals(credentials)) {
            return;
        }

        // 匹配密码
        if (this.getPasswordEncoder().matches(credentials.toString(), userDetails.getPassword())) {
            return;
        }

        throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
}
