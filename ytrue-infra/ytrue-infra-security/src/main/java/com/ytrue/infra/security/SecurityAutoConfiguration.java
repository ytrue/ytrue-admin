package com.ytrue.infra.security;

import com.ytrue.infra.security.annotation.IgnoreWebSecurity;
import com.ytrue.infra.security.constant.HttpRequestType;
import com.ytrue.infra.security.dao.LoginAuthenticationProvider;
import com.ytrue.infra.security.filter.JwtAuthenticationTokenFilter;
import com.ytrue.infra.security.handler.AccessDeniedHandlerImpl;
import com.ytrue.infra.security.handler.AuthenticationEntryPointImpl;
import com.ytrue.infra.security.handler.LogoutHandlerImpl;
import com.ytrue.infra.security.handler.LogoutSuccessHandlerImpl;
import com.ytrue.infra.security.integration.IntegrationAuthenticationFilter;
import com.ytrue.infra.security.permission.PermissionService;
import com.ytrue.infra.security.properties.IgnoreWebSecurityProperties;
import com.ytrue.infra.security.properties.JwtProperties;
import com.ytrue.infra.security.properties.SecurityProperties;
import com.ytrue.infra.security.service.LoginService;
import com.ytrue.infra.security.service.impl.LoginServiceImpl;
import com.ytrue.infra.security.service.impl.UserDetailsServiceImpl;
import com.ytrue.infra.security.util.JwtOperation;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/5/12 14:05
 * @description SecurityAutoConfiguration
 */
@Configuration
@EnableMethodSecurity
public class SecurityAutoConfiguration {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public JwtOperation jwtOperation() {
        return new JwtOperation();
    }


    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }


    @Bean("pms")
    public PermissionService  permissionService(){
        return new PermissionService();
    }

    @Bean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public IgnoreWebSecurityProperties ignoreWebSecurityProperties() {
        return new IgnoreWebSecurityProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginService loginService(HttpSecurity httpSecurity) throws Exception {
        // 这个玩法不知道对不对
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
        return new LoginServiceImpl(authenticationManager, stringRedisTemplate, jwtOperation(), securityProperties(), jwtProperties());
    }


    @Bean
    @ConditionalOnMissingBean
    public LogoutHandlerImpl logoutHandler() {
        return new LogoutHandlerImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }


    /**
     * 自定义LoginAuthenticationProvider继承DaoAuthenticationProvider重写
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
        // 这个地方一定要对userDetailsService，passwordEncoder赋值，会错误
        loginAuthenticationProvider.setUserDetailsService(userDetailsService());
        loginAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return loginAuthenticationProvider;
    }


    /**
     * 注册自己的UserDetailsServiceImpl
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    /**
     * 添加 token 认证拦截器
     *
     * @return JwtAuthenticationTokenFilter
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(stringRedisTemplate, jwtOperation(), securityProperties());
    }

    /**
     * 添加集成认证拦截器
     *
     * @return IntegrationAuthenticationFilter
     */
    @Bean
    public IntegrationAuthenticationFilter integrationAuthenticationFilter() {
        return new IntegrationAuthenticationFilter(securityProperties());
    }


    /**
     * SecurityContextHolder还有其他两种模式，分别为
     * SecurityContextHolder.MODE_GLOBAL
     * SecurityContextHolder.MODE_INHERITABLETHREADLOCAL
     * 前者表示SecurityContextHolder对象的全局的，应用中所有线程都可以访问；
     * 后者用于线程有父子关系的情境中，线程希望自己的子线程和自己有相同的安全性
     *
     * @return
     */
    @Bean
    public InitializingBean initializingBean() {
        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    /**
     * 进行配置
     *
     * @param http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //不通过Session获取SecurityContext
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 获取配置
        Map<String, HashSet<String>> ignoreAuthConfig = initIgnoreAuthConfig();
        // 允许匿名访问
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers(
                        HttpMethod.GET, ignoreAuthConfig.get(HttpRequestType.GET).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.POST, ignoreAuthConfig.get(HttpRequestType.POST).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.DELETE, ignoreAuthConfig.get(HttpRequestType.DELETE).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.PUT, ignoreAuthConfig.get(HttpRequestType.PUT).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.HEAD, ignoreAuthConfig.get(HttpRequestType.HEAD).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.PATCH, ignoreAuthConfig.get(HttpRequestType.PATCH).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.OPTIONS, ignoreAuthConfig.get(HttpRequestType.OPTIONS).toArray(new String[0]))
                .anonymous().requestMatchers(HttpMethod.TRACE, ignoreAuthConfig.get(HttpRequestType.TRACE).toArray(new String[0]))
                .anonymous().requestMatchers(ignoreAuthConfig.get(HttpRequestType.PATTERN).toArray(new String[0])).anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated());


        // spring security使用X-Frame-Options防止网页被Frame 这里禁用掉
        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        // 这里禁用登出，让用户自定义处理
        http.logout(AbstractHttpConfigurer::disable);

        //配置异常处理器
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint())
                //配置授权失败处理器
                .accessDeniedHandler(accessDeniedHandler()));

        //添加过滤器
        http.addFilterBefore(integrationAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter(), IntegrationAuthenticationFilter.class);

        //允许跨域
        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }


    @Getter
    private static Map<String, HashSet<String>> ignoreAuthConfigMap = new HashMap<>();

    /**
     * 配置
     *
     * @return
     */
    private Map<String, HashSet<String>> initIgnoreAuthConfig() {
        // get    ==> /xx/xxx
        // post   ==> /xx/xxx
        // delete ==> /xx/xxx
        Map<String, HashSet<String>> ignoreRequestUrlMap = new HashMap<>(16);

        // 登录的
        String authUrl = securityProperties().getAuthUrl();
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATTERN, this::newSet).add(authUrl);

        // 读取配置文件的
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.GET, this::newSet).addAll(ignoreWebSecurityProperties().getGet());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.POST, this::newSet).addAll(ignoreWebSecurityProperties().getPost());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.DELETE, this::newSet).addAll(ignoreWebSecurityProperties().getDelete());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PUT, this::newSet).addAll(ignoreWebSecurityProperties().getPut());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.HEAD, this::newSet).addAll(ignoreWebSecurityProperties().getHead());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATCH, this::newSet).addAll(ignoreWebSecurityProperties().getPatch());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.OPTIONS, this::newSet).addAll(ignoreWebSecurityProperties().getOptions());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.TRACE, this::newSet).addAll(ignoreWebSecurityProperties().getTrace());
        ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATTERN, this::newSet).addAll(ignoreWebSecurityProperties().getPattern());

        // 读取注解上面的
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(IgnoreWebSecurity.class)) {
                continue;
            }

            Set<String> patternValues = entry.getKey().getPatternValues();
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
            if (CollectionUtils.isEmpty(methods)) {
                // RequestMapping没有指定method
                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATTERN, this::newSet).addAll(patternValues);
            } else {
                for (RequestMethod method : methods) {
                    // RequestMapping指定了method
                    switch (method.name()) {
                        case HttpRequestType.GET ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.GET, this::newSet).addAll(patternValues);
                        case HttpRequestType.POST ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.POST, this::newSet).addAll(patternValues);
                        case HttpRequestType.PUT ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PUT, this::newSet).addAll(patternValues);
                        case HttpRequestType.DELETE ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.DELETE, this::newSet).addAll(patternValues);
                        case HttpRequestType.PATCH ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATCH, this::newSet).addAll(patternValues);
                        case HttpRequestType.OPTIONS ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.OPTIONS, this::newSet).addAll(patternValues);
                        case HttpRequestType.HEAD ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.HEAD, this::newSet).addAll(patternValues);
                        case HttpRequestType.TRACE ->
                                ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.TRACE, this::newSet).addAll(patternValues);
                        default -> throw new RuntimeException("请求类型不支持");
                    }
                }
            }

        }

        // 存一份
        ignoreAuthConfigMap = ignoreRequestUrlMap;
        return ignoreRequestUrlMap;
    }

    private HashSet<String> newSet(String str) {
        return new HashSet<>();
    }


}
