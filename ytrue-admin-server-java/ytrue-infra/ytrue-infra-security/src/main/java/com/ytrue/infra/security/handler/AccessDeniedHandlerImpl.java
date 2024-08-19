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


    /**
     * 如果 使用了    @ExceptionHandler 处理 AccessDeniedException 异常 这里是不会生效的
     * @param request that resulted in an <code>AccessDeniedException</code>
     * @param response so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws ServletException
     * @throws IOException
     */
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
