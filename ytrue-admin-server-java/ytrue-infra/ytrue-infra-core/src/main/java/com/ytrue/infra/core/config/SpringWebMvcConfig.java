package com.ytrue.infra.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * SpringWebMvcConfig 类用于配置 Spring MVC 的相关设置。
 *
 * <p>
 * 包括静态资源处理、跨域配置和消息转换器的配置等。
 * </p>
 *
 * @author ytrue
 * @date 2022/1/20 14:33
 */
@EnableWebMvc
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer {

    /**
     * 注册静态资源处理器，以便访问相关文档和资源。
     *
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文档相关
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        // favicon.ico
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/favicon.ico");
        // 本地图片访问
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/storage");
        // jar包后的访问
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", "file:static/");
    }

    /**
     * 配置跨域请求的相关设置。
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许带有 cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的 header 属性
                .allowedHeaders("*")
                // 跨域允许的时间
                .maxAge(3600);
    }

    /**
     * Jackson 对象映射器
     */
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 配置消息转换器。
     *
     * <p>
     * 移除默认的 MappingJackson2HttpMessageConverter，并使用自定义的 ObjectMapper。
     * </p>
     *
     * @param converters 消息转换器列表
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(o -> o instanceof MappingJackson2HttpMessageConverter);
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
