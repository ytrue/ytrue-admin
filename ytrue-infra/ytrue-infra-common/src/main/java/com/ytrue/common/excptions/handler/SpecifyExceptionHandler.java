package com.ytrue.common.excptions.handler;

import com.ytrue.common.utils.ApiResultResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author ytrue
 * @date 2023/1/2 18:02
 * @description SpecifyExceptionHandler
 */

@RestControllerAdvice
public class SpecifyExceptionHandler {


    @ExceptionHandler(BindException.class)
    public ApiResultResponse<Object> validExceptionHandler(final Exception e) {

        // TODO 待完善

        BindException exception = (BindException) e;
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            System.out.println(error.getField());
            System.out.println(error.getDefaultMessage());

        }

        return ApiResultResponse.fail(4000,"error");
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResultResponse<Object> constraintViolationExceptionHandler(final Exception e) {
        ConstraintViolationException exception = (ConstraintViolationException) e;
        // TODO 待完善
        return ApiResultResponse.fail(4000, exception.getMessage());
    }
}
