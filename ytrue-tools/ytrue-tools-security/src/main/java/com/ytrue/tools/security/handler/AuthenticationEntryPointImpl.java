package com.ytrue.tools.security.handler;

import com.ytrue.tools.security.excptions.AuthenticationFailureException;
import com.ytrue.tools.security.util.ServletWebRequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author ytrue
 * @date 2022/4/26 10:29
 * @description 如果是认证过程中出现的异常会被封装成AuthenticationException然后调用AuthenticationEntryPoint对象的方法去进行异常处理。
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws RuntimeException, ServletException, IOException {
        // springboot3没有办法直接抛异常了
        ServletWebRequestUtil.errorPathForward(
                request,
                response,
                new AuthenticationFailureException(authException),
                4000);
    }


}
