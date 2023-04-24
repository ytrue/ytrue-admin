package com.ytrue.tools.security;

import com.ytrue.tools.security.authentication.dao.LoginAuthenticationProvider;
import com.ytrue.tools.security.filter.JwtAuthenticationTokenFilter;
import com.ytrue.tools.security.handler.AccessDeniedHandlerImpl;
import com.ytrue.tools.security.handler.AuthenticationEntryPointImpl;
import com.ytrue.tools.security.handler.LogoutHandlerImpl;
import com.ytrue.tools.security.handler.LogoutSuccessHandlerImpl;
import com.ytrue.tools.security.integration.IntegrationAuthenticationFilter;
import com.ytrue.tools.security.integration.authenticator.IntegrationAuthenticator;
import com.ytrue.tools.security.properties.JwtProperties;
import com.ytrue.tools.security.properties.SecurityProperties;
import com.ytrue.tools.security.service.LoginService;
import com.ytrue.tools.security.service.impl.LoginServiceImpl;
import com.ytrue.tools.security.service.impl.UserDetailsServiceImpl;
import com.ytrue.tools.security.util.JwtOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/5/12 14:05
 * @description SecurityAutoConfiguration
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

        return new LoginServiceImpl(
                this.authenticationManager(),
                stringRedisTemplate,
                jwtOperation(),
                securityProperties(),
                jwtProperties()
        );
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

        // 允许匿名访问URL
        Set<String> ignoreAuth = securityProperties().getIgnoreAuth();
        ignoreAuth.add(securityProperties().getAuthUrl());
        String[] stringArray = new String[ignoreAuth.size()];
        String[] ignoreAuthArray = ignoreAuth.toArray(stringArray);
        http.authorizeRequests()
                // 允许匿名访问
                .antMatchers(ignoreAuthArray).anonymous()
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

}
