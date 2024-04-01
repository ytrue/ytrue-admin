package com.ytrue.tools.security.util;

import com.ytrue.tools.security.excptions.AuthenticationFailureException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

/**
 * @author ytrue
 * @date 2023-08-06 14:24
 * @description ServletWebRequestUtil
 */
@Slf4j
public class ServletWebRequestUtil {

    /**
     * error path转发错误
     *
     * @param request
     * @param response
     * @param e
     * @param statusCode
     * @throws ServletException
     * @throws IOException
     */
    public static void errorPathForward(
            HttpServletRequest request,
            HttpServletResponse response,
            Throwable e,
            int statusCode
    ) throws ServletException, IOException {

        // 这里转发给全局异常处理，springboot3捕获不到拦截器异常了
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        servletWebRequest.setAttribute("jakarta.servlet.error.status_code", statusCode, RequestAttributes.SCOPE_REQUEST);
        servletWebRequest.setAttribute("jakarta.servlet.error.message", e.getMessage(), RequestAttributes.SCOPE_REQUEST);
        servletWebRequest.setAttribute("jakarta.servlet.error.exception", e, RequestAttributes.SCOPE_REQUEST);
        servletWebRequest.setAttribute("jakarta.servlet.error.request_uri", request.getRequestURI(), RequestAttributes.SCOPE_REQUEST);


        HttpServletRequest servletRequest = servletWebRequest.getNativeRequest(HttpServletRequest.class);
        // 转发
        RequestDispatcher requestDispatcher = servletRequest.getRequestDispatcher("/error");
        requestDispatcher.forward(servletRequest, response);
    }
}
