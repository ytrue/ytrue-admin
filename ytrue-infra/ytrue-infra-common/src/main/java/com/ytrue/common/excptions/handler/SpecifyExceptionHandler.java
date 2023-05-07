package com.ytrue.common.excptions.handler;

import com.ytrue.common.utils.ApiResultResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author ytrue
 * @date 2023/1/2 18:02
 * @description SpecifyExceptionHandler
 */

@RestControllerAdvice
public class SpecifyExceptionHandler {


    /**
     * Validated 注解写在方法上的时候会报这个异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ApiResultResponse<Object> bindExceptionHandler(final BindException exception) {
        // 这个不是按顺序来的
        FieldError fieldError = exception.getFieldErrors().stream().findFirst().orElse(null);
        assert fieldError != null;
        String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        return ApiResultResponse.fail(4000, message);
    }


    /**
     * Validated 注解写在类上的时候会报这个异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResultResponse<Object> constraintViolationExceptionHandler(final ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream().findFirst().map(ConstraintViolation::getMessage).orElse("");
        return ApiResultResponse.fail(4000, message);
    }
}
