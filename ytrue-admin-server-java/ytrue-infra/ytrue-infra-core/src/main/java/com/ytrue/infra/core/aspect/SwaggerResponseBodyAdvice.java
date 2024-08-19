package com.ytrue.infra.core.aspect;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Base64;
import java.util.Map;

/**
 * @author ytrue
 * @date 2023-11-23 16:17
 * @description 针对自定义baseMapper swagger /v3/api-docs/xxx 返回的base64数据
 */
@ConditionalOnExpression(value = "${springdoc.api-docs.enable:true} or ${knife4j.production:true}")
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class SwaggerResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    private final static String BASE_SWAGGER_PATH = "/v3/api-docs";

    private final static String SWAGGER_CONFIG_PATH = BASE_SWAGGER_PATH + "/swagger-config";


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        String path = request.getURI().getPath();
        // 正对swagger做处理
        if (path.startsWith(BASE_SWAGGER_PATH) && !path.equals(SWAGGER_CONFIG_PATH)) {
            String base64encodedString = Base64.getEncoder().encodeToString((byte[]) body);
            // 获取json格式字符串
            String jsonString = new String(Base64.getDecoder().decode(base64encodedString.getBytes()));
            // 转换成map
            try {
                return objectMapper.readValue(jsonString, Map.class);
            } catch (JsonProcessingException e) {
                log.error("转换map object失败", e);
                return null;
            }
        }

        return body;
    }
}
