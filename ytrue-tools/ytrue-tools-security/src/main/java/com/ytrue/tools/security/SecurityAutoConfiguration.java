package com.ytrue.tools.security;

import com.ytrue.tools.security.annotation.IgnoreWebSecurity;
import com.ytrue.tools.security.authentication.dao.LoginAuthenticationProvider;
import com.ytrue.tools.security.constant.HttpRequestType;
import com.ytrue.tools.security.filter.JwtAuthenticationTokenFilter;
import com.ytrue.tools.security.handler.AccessDeniedHandlerImpl;
import com.ytrue.tools.security.handler.AuthenticationEntryPointImpl;
import com.ytrue.tools.security.handler.LogoutHandlerImpl;
import com.ytrue.tools.security.handler.LogoutSuccessHandlerImpl;
import com.ytrue.tools.security.integration.IntegrationAuthenticationFilter;
import com.ytrue.tools.security.integration.authenticator.IntegrationAuthenticator;
import com.ytrue.tools.security.properties.IgnoreWebSecurityProperties;
import com.ytrue.tools.security.properties.JwtProperties;
import com.ytrue.tools.security.properties.SecurityProperties;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.service.impl.LoginServiceImpl;
import com.ytrue.tools.security.service.impl.UserDetailsServiceImpl;
import com.ytrue.tools.security.util.JwtOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ytrue
 * @date 2022/5/12 14:05
 * @description SecurityAutoConfiguration
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

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
    public LoginService loginService() throws Exception {

        return new LoginServiceImpl(this.authenticationManager(), stringRedisTemplate, jwtOperation(), securityProperties(), jwtProperties());
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
    @Override
    public UserDetailsService userDetailsService() {
        // WebSecurityConfigurerAdapter类有已经有ApplicationContext，不要再去实现 getApplicationContextAware，不然会空指针
        Map<String, IntegrationAuthenticator> integrationAuthenticatorMap = getApplicationContext().getBeansOfType(IntegrationAuthenticator.class);

        ArrayList<IntegrationAuthenticator> integrationAuthenticators = new ArrayList<>();

        integrationAuthenticatorMap.forEach((key, integrationAuthenticator) -> integrationAuthenticators.add(integrationAuthenticator));

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setIntegrationAuthenticators(integrationAuthenticators);

        return userDetailsService;
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
     * 进行配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 获取配置
        Map<String, HashSet<String>> ignoreAuthConfig = ignoreAuthConfig();
        // 允许匿名访问
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, ignoreAuthConfig.get(HttpRequestType.GET).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.POST, ignoreAuthConfig.get(HttpRequestType.POST).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.DELETE, ignoreAuthConfig.get(HttpRequestType.DELETE).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.PUT, ignoreAuthConfig.get(HttpRequestType.PUT).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.HEAD, ignoreAuthConfig.get(HttpRequestType.HEAD).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.PATCH, ignoreAuthConfig.get(HttpRequestType.PATCH).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.OPTIONS, ignoreAuthConfig.get(HttpRequestType.OPTIONS).toArray(new String[0])).anonymous()
                .antMatchers(HttpMethod.TRACE, ignoreAuthConfig.get(HttpRequestType.TRACE).toArray(new String[0])).anonymous()
                .antMatchers(ignoreAuthConfig.get(HttpRequestType.PATTERN).toArray(new String[0])).anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // spring security使用X-Frame-Options防止网页被Frame 这里禁用掉
        http.headers().frameOptions().disable();

        // 这里禁用登出，让用户自定义处理
        http.logout().disable();

        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint())
                //配置授权失败处理器
                .accessDeniedHandler(accessDeniedHandler());

        //添加过滤器
        http.addFilterBefore(integrationAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter(), IntegrationAuthenticationFilter.class);

        //允许跨域
        http.cors();
    }


    /**
     * 配置
     *
     * @return
     */
    private Map<String, HashSet<String>> ignoreAuthConfig() {

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
            if (handlerMethod.hasMethodAnnotation(IgnoreWebSecurity.class)) {
                Set<String> patternValues = entry.getKey().getPatternValues();
                Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
                if (CollectionUtils.isEmpty(methods)) {
                    // RequestMapping没有指定method
                    ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATTERN, this::newSet).addAll(patternValues);
                } else {
                    for (RequestMethod method : methods) {
                        // RequestMapping指定了method
                        if (method.name().equals(HttpRequestType.GET)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.GET, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.POST)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.POST, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.PUT)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PUT, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.DELETE)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.DELETE, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.PATCH)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.PATCH, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.OPTIONS)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.OPTIONS, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.HEAD)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.HEAD, this::newSet).addAll(patternValues);
                        } else if (method.name().equals(HttpRequestType.TRACE)) {
                            ignoreRequestUrlMap.computeIfAbsent(HttpRequestType.TRACE, this::newSet).addAll(patternValues);
                        } else {
                            throw new RuntimeException("请求类型不支持");
                        }
                    }
                }
            }
        }

        return ignoreRequestUrlMap;
    }

    private HashSet<String> newSet(String str) {
        return new HashSet<>();
    }

}
