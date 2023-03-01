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

        /*
            请求进来 会按照 filter -> interceptor -> controllerAdvice -> aspect -> controller的顺序调用

            当controller返回异常 也会按照controller -> aspect -> controllerAdvice -> interceptor -> filter来依次抛出

            这种Filter发生的404、405、500错误都会到Spring默认的异常处理。
            如果你在配置文件配置了server.error.path的话，就会使用你配置的异常处理地址，
            如果没有就会使用你配置的error.path路径地址，如果还是没有，默认使用/error来作为发生异常的处理地址。
            如果想要替换默认的非Controller异常处理直接实现Spring提供的ErrorController接口就行了
            而 spring security是基于过滤器实现认证
            参考文章:
            https://www.jianshu.com/p/ec932d6091c1
            https://blog.csdn.net/weixin_43702146/article/details/118606502
         */
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
