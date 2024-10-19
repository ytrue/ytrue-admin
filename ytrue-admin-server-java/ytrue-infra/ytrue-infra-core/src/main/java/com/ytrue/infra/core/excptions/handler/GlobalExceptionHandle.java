package com.ytrue.infra.core.excptions.handler;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.ThrowableUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * GlobalExceptionHandle 是全局异常处理类，负责捕获和处理应用程序中的所有异常。
 * <p>
 * 该类实现了 Spring 的 ErrorController 接口，以提供统一的错误响应。
 * 它能够处理从拦截器、Security 模块以及其他组件抛出的异常。
 * </p>
 *
 * @author yangyi
 * @date 2022/6/15 16:27
 * @description 全局异常处理类
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
public class GlobalExceptionHandle implements ErrorController {

    private final ErrorAttributes errorAttributes;

    /**
     * 处理全局异常并返回错误响应。
     * 该方法会捕获异常信息并设置 HTTP 状态码为 200，以方便前端处理。
     *
     * @param response HTTP 响应对象
     * @param req      Web 请求对象
     * @return 包含错误码和错误信息的 ServerResponseEntity 对象
     */
    @RequestMapping("error")
    @ResponseBody
    public ServerResponseEntity<Void> error(HttpServletResponse response, WebRequest req) {
        // 设置 HTTP 状态码为 200
        response.setStatus(HttpStatus.OK.value());

        // 获取错误属性
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(
                req,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.STACK_TRACE,
                        ErrorAttributeOptions.Include.BINDING_ERRORS
                )
        );

        // 获取错误信息和状态码
        String errorMessage = errorAttributes.get("message").toString();
        String errorCode = errorAttributes.get("status").toString();

        // 针对 BaseCodeException 的处理
        Throwable error = this.errorAttributes.getError(req);
        if (error instanceof BaseCodeException baseCodeException) {
            errorMessage = error.getMessage();
            errorCode = baseCodeException.getCode();
        }

        // 打印错误日志
        printErrorLog(error, errorCode, errorMessage);

        // 返回失败响应
        return ServerResponseEntity.fail(errorCode, errorMessage);
    }

    /**
     * 打印错误日志信息。
     *
     * @param error       捕获的异常
     * @param errorCode   错误码
     * @param errorMessage 错误信息
     */
    private void printErrorLog(Throwable error, String errorCode, String errorMessage) {
        String errorPrintTemplate = "GlobalExceptionHandle get errorMessage：{} get errorCode {}";

        if (error != null) {
            if (error instanceof BaseCodeException) {
                log.error(errorPrintTemplate + " get error：{}", errorMessage, errorCode,
                        ThrowableUtil.getStackTraceByPn(error, "com.ytrue"));
            } else {
                log.error(errorPrintTemplate + " get error：", errorMessage, errorCode, error);
            }
        } else {
            log.error(errorPrintTemplate, errorMessage, errorCode);
        }
    }
}
