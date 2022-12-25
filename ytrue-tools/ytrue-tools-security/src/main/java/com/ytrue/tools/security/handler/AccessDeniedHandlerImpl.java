package com.ytrue.tools.security.handler;

import com.ytrue.tools.security.excptions.AuthenticationFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ytrue
 * @date 2022/4/26 10:29
 * @description 如果是授权过程中出现的异常会被封装成AccessDeniedException然后调用AccessDeniedHandler对象的方法去进行异常处理
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {


    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) {
        throw new AuthenticationFailureException(accessDeniedException);
    }
}
