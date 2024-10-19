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
 * @date 2023-11-23
 * @description SwaggerResponseBodyAdvice 类用于处理 Swagger 文档的响应体。
 *
 *
 * <p>
 * 针对自定义 baseMapper 返回的 base64 数据进行处理，
 * 以便在 Swagger UI 中正确显示接口文档。
 * </p>
 *

 */
@ConditionalOnExpression(value = "${springdoc.api-docs.enable:true} or ${knife4j.production:true}")
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class SwaggerResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper; // Jackson 对象映射器

    /**
     * 确定该适配器是否支持给定的返回类型和转换器类型。
     *
     * @param returnType 返回的方法参数信息
     * @param converterType 处理消息的转换器类型
     * @return 如果支持返回 true；否则返回 false
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // 目前所有情况都支持
    }


    /**
     * Swagger 文档的基本路径
     */
    private final static String BASE_SWAGGER_PATH = "/v3/api-docs";

    /**
     * Swagger 配置路径
     */
    private final static String SWAGGER_CONFIG_PATH = BASE_SWAGGER_PATH + "/swagger-config";

    /**
     * 在响应体写入之前对其进行处理。
     *
     * @param body 响应体内容
     * @param returnType 返回的方法参数信息
     * @param selectedContentType 选择的内容类型
     * @param selectedConverterType 选择的消息转换器类型
     * @param request 当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @return 处理后的响应体内容
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        String path = request.getURI().getPath();
        // 针对 Swagger API 文档路径进行处理
        if (path.startsWith(BASE_SWAGGER_PATH) && !path.equals(SWAGGER_CONFIG_PATH)) {
            // 将响应体转换为 base64 编码字符串
            String base64encodedString = Base64.getEncoder().encodeToString((byte[]) body);
            // 获取 JSON 格式字符串
            String jsonString = new String(Base64.getDecoder().decode(base64encodedString.getBytes()));
            // 转换成 Map 对象
            try {
                return objectMapper.readValue(jsonString, Map.class);
            } catch (JsonProcessingException e) {
                log.error("转换 map 对象失败", e);
                return null; // 转换失败，返回 null
            }
        }

        return body; // 如果不符合条件，则返回原始响应体
    }
}
