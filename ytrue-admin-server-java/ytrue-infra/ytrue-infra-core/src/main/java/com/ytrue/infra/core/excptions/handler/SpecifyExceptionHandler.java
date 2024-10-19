package com.ytrue.infra.core.excptions.handler;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.response.ServerResponseInfoEnum;
import com.ytrue.infra.core.util.ClassUtil;
import com.ytrue.infra.core.util.ThrowableUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * SpecifyExceptionHandler 是一个全局异常处理类，用于处理特定的异常并返回统一的错误响应。
 * <p>
 * 此类会捕获绑定异常、约束违反异常以及其他所有未处理的异常，确保在出现异常时提供明确的错误信息和状态码。
 * </p>
 *
 * @author ytrue
 * @date 2023/1/2 18:02
 * @description SpecifyExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class SpecifyExceptionHandler {

    /**
     * 处理 BindException，通常是当 @Validated 注解应用在方法参数上时抛出的异常。
     *
     * @param exception BindException 异常对象
     * @return 统一的失败响应，包含错误信息和状态码
     */
    @ExceptionHandler(BindException.class)
    public ServerResponseEntity<Void> bindExceptionHandler(final BindException exception) {
        // 获取第一个字段错误
        FieldError fieldError = exception.getFieldErrors().stream().findFirst().orElse(null);
        assert fieldError != null;
        String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        return ServerResponseEntity.fail(ServerResponseInfoEnum.BAD_REQUEST.code(), message);
    }

    /**
     * 处理 ConstraintViolationException，通常是当 @Validated 注解应用在类上时抛出的异常。
     *
     * @param exception ConstraintViolationException 异常对象
     * @return 统一的失败响应，包含错误信息和状态码
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ServerResponseEntity<Void> constraintViolationExceptionHandler(final ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream().findFirst().map(ConstraintViolation::getMessage).orElse("");
        return ServerResponseEntity.fail(ServerResponseInfoEnum.BAD_REQUEST.code(), message);
    }

    /**
     * 处理所有其他未捕获的异常。
     *
     * @param error 捕获的异常
     * @return 统一的失败响应，包含错误信息和状态码
     */
    @ExceptionHandler({Exception.class})
    public ServerResponseEntity<Void> exceptionHandler(final Exception error) {
        String errorMessage = error.getMessage();
        String errorCode = ServerResponseInfoEnum.INTERNAL_SERVER_ERROR.code();

        // 如果捕获的异常是 BaseCodeException，则获取其特定的错误码
        if (error instanceof BaseCodeException baseCodeException) {
            errorCode = baseCodeException.getCode();
        }

        // 检查 AccessDeniedException 类是否存在，若存在则返回403错误
        String accessDeniedExceptionClassName = "org.springframework.security.access.AccessDeniedException";
        if (ClassUtil.isClassPresentAndSubclass(accessDeniedExceptionClassName, error.getClass(), this.getClass().getClassLoader())) {
            return ServerResponseEntity.fail(ServerResponseInfoEnum.FORBIDDEN.code(), ServerResponseInfoEnum.FORBIDDEN.message());
        }

        // 打印错误日志
        printErrorLog(error, errorCode, errorMessage);
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
        String errorPrintTemplate = "SpecifyExceptionHandler get errorMessage：{} get errorCode {} get error：";
        if (error instanceof BaseCodeException) {
            log.error(errorPrintTemplate + "{}", errorMessage, errorCode, ThrowableUtil.getStackTraceByPn(error, "com.ytrue"));
        } else {
            log.error(errorPrintTemplate, errorMessage, errorCode, error);
        }
    }
}
