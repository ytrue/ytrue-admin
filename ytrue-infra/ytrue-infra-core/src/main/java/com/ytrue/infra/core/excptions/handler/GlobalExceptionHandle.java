package com.ytrue.infra.core.excptions.handler;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.ThrowableUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @author yangyi
 * @date 2022/6/15 16:27
 * @description 全局异常处理类
 */
@Controller
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class GlobalExceptionHandle implements ErrorController {

    private final ErrorAttributes errorAttributes;

    /**
     * 这里主要捕获，像拦截器这样的异常，还没有dispatcherServlet的异常
     * security 模块出现错误也会进入这里
     * 全局异常处理
     *
     * @param response
     * @param req
     * @return
     */
    @RequestMapping("error")
    @ResponseBody
    public ServerResponseEntity<Object> error(HttpServletResponse response, WebRequest req) {
        //设置200，方便前端处理
        response.setStatus(200);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(req, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.BINDING_ERRORS));
        //返回错误
        String errorMessage = errorAttributes.get("message").toString();
        String errorCode = errorAttributes.get("status").toString();

        //针对BaseCodeException处理
        Throwable error = this.errorAttributes.getError(req);

        if (error instanceof BaseCodeException baseCodeException) {
            errorMessage = error.getMessage();
            errorCode = baseCodeException.getCode();
        }

        // 打印错误日志
        printErrorLog(error, errorCode, errorMessage);
        return ServerResponseEntity.fail(errorCode, errorMessage);
    }

    /**
     * 打印日志
     *
     * @param error
     * @param errorCode
     * @param errorMessage
     */
    private void printErrorLog(Throwable error, String errorCode, String errorMessage) {
        String errorPrintTemplate = "GlobalExceptionHandle get errorMessage：{} get errorCode {}";
        if (error != null) {
            if (error instanceof BaseCodeException) {
                log.error(errorPrintTemplate + " get error：{}", errorMessage, errorCode, ThrowableUtil.getStackTraceByPn(error, "com.ytrue"));
            } else {
                log.error(errorPrintTemplate + " get error：", errorMessage, errorCode, error);
            }

        } else {
            log.error(errorPrintTemplate, errorMessage, errorCode);
        }
    }
}