package com.ytrue.tools.security.authentication.dao;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

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
        if (authentication.getCredentials() != null && !"".equals(authentication.getCredentials())) {
            if (!this.getPasswordEncoder().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }
}
