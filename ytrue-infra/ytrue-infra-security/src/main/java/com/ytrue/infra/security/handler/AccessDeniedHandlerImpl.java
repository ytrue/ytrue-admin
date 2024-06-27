package com.ytrue.infra.security.handler;

import com.ytrue.infra.security.excptions.AccessDeniedFailureException;
import com.ytrue.infra.security.util.ServletWebRequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


/**
 * @author ytrue
 * @date 2022/4/26 10:29
 * @description 如果是授权过程中出现的异常会被封装成AccessDeniedException然后调用AccessDeniedHandler对象的方法去进行异常处理
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws ServletException, IOException {
        // springboot3没有办法直接抛异常了
        ServletWebRequestUtil.errorPathForward(
                request,
                response,
                new AccessDeniedFailureException(accessDeniedException),
                403);
    }
}
