package com.ytrue.tools.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ytrue
 * @date 2022/4/26 10:29
 * @description 如果是认证过程中出现的异常会被封装成AuthenticationException然后调用AuthenticationEntryPoint对象的方法去进行异常处理。
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws RuntimeException {

        //密码对比错误
        if (authException instanceof BadCredentialsException) {
            throw new BadCredentialsException(authException.getMessage());
        }

        // TODO 这里异常类型要替换下，之后全局一次要做code处理
        throw new InternalAuthenticationServiceException(authException.getMessage(), authException);
    }
}
