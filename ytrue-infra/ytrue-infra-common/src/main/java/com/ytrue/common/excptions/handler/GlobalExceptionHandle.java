package com.ytrue.common.excptions.handler;


import com.ytrue.common.base.BaseCodeException;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.tools.security.excptions.AuthenticationFailureException;
import com.ytrue.tools.security.excptions.AuthorizationFailureException;
import com.ytrue.tools.security.excptions.InvalidTokenException;
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

import javax.servlet.http.HttpServletResponse;
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
     * 全局异常处理
     *
     * @param response
     * @param req
     * @return
     */
    @RequestMapping("error")
    @ResponseBody
    public ApiResultResponse<Object> error(HttpServletResponse response, WebRequest req) {
        //设置200，方便前端处理
        response.setStatus(200);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(req,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.STACK_TRACE,
                        ErrorAttributeOptions.Include.BINDING_ERRORS
                ));
        //返回错误
        String errorMessage = (String) errorAttributes.get("message");
        Integer errorCode = (Integer) errorAttributes.get("status");

        //针对BaseCodeException处理
        Throwable error = this.errorAttributes.getError(req);
        if (error instanceof BaseCodeException) {
            errorMessage = error.getMessage();
            errorCode = ((BaseCodeException) error).getCode();
        }

        // 没有的登录
        if (error instanceof AuthenticationFailureException) {
            errorMessage = ResponseCode.NO_LOGIN.getMessage();
            errorCode = ResponseCode.NO_LOGIN.getCode();
        }

        // 无效token
        if (error instanceof InvalidTokenException) {
            errorMessage = ResponseCode.INVALID_TOKEN.getMessage();
            errorCode = ResponseCode.INVALID_TOKEN.getCode();
        }

        // 没有权限
        if (error instanceof AuthorizationFailureException) {
            errorMessage = error.getMessage();
            errorCode = ResponseCode.PERMISSION_DENIED.getCode();
        }

        return ApiResultResponse.fail(errorCode, errorMessage);
    }
}
