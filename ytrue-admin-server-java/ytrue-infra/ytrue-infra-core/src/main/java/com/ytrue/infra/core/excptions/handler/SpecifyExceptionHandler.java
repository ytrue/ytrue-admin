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
 * @author ytrue
 * @date 2023/1/2 18:02
 * @description SpecifyExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class SpecifyExceptionHandler {


    /**
     * Validated 注解写在方法上的时候会报这个异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ServerResponseEntity<Void> bindExceptionHandler(final BindException exception) {
        // 这个不是按顺序来的
        FieldError fieldError = exception.getFieldErrors().stream().findFirst().orElse(null);
        assert fieldError != null;
        String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        return ServerResponseEntity.fail(ServerResponseInfoEnum.BAD_REQUEST.code(), message);
    }


    /**
     * Validated 注解写在类上的时候会报这个异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ServerResponseEntity<Void> constraintViolationExceptionHandler(final ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream().findFirst().map(ConstraintViolation::getMessage).orElse("");
        return ServerResponseEntity.fail(ServerResponseInfoEnum.BAD_REQUEST.code(), message);
    }

    @ExceptionHandler({Exception.class})
    public ServerResponseEntity<Void> exceptionHandler(final Exception error) {

        String errorMessage = error.getMessage();
        String errorCode = ServerResponseInfoEnum.INTERNAL_SERVER_ERROR.code();

        if (error instanceof BaseCodeException baseCodeException) {
            errorCode = baseCodeException.getCode();
        }


        // 反射检查 AccessDeniedHandler 类是否存在
        String accessDeniedExceptionClassName = "org.springframework.security.access.AccessDeniedException";
        if (ClassUtil.isClassPresentAndSubclass(accessDeniedExceptionClassName, error.getClass(), this.getClass().getClassLoader())) {
            return ServerResponseEntity.fail(ServerResponseInfoEnum.FORBIDDEN.code(), ServerResponseInfoEnum.FORBIDDEN.message());
        }

        // 打印日志
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
        String errorPrintTemplate = "SpecifyExceptionHandler get errorMessage：{} get errorCode {} get error：";
        if (error instanceof BaseCodeException) {
            log.error(errorPrintTemplate + "{}", errorMessage, errorCode, ThrowableUtil.getStackTraceByPn(error, "com.ytrue"));
        } else {
            log.error(errorPrintTemplate, errorMessage, errorCode, error);
        }
    }
}
