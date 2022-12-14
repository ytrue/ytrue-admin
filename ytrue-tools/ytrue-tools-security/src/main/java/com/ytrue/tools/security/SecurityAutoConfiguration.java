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
     * ?????????LoginAuthenticationProvider??????DaoAuthenticationProvider??????
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
        // ????????????????????????userDetailsService???passwordEncoder??????????????????
        loginAuthenticationProvider.setUserDetailsService(userDetailsService());
        loginAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return loginAuthenticationProvider;
    }


    /**
     * ???????????????UserDetailsServiceImpl
     *
     * @return UserDetailsService
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // WebSecurityConfigurerAdapter???????????????ApplicationContext????????????????????? getApplicationContextAware?????????????????????
        Map<String, IntegrationAuthenticator> integrationAuthenticatorMap = getApplicationContext().getBeansOfType(IntegrationAuthenticator.class);

        ArrayList<IntegrationAuthenticator> integrationAuthenticators = new ArrayList<>();

        integrationAuthenticatorMap.forEach((key, integrationAuthenticator) -> integrationAuthenticators.add(integrationAuthenticator));

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setIntegrationAuthenticators(integrationAuthenticators);

        return userDetailsService;
    }


    /**
     * ?????? token ???????????????
     *
     * @return JwtAuthenticationTokenFilter
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(stringRedisTemplate, jwtOperation(), securityProperties());
    }

    /**
     * ???????????????????????????
     *
     * @return IntegrationAuthenticationFilter
     */
    @Bean
    public IntegrationAuthenticationFilter integrationAuthenticationFilter() {
        return new IntegrationAuthenticationFilter(securityProperties());
    }


    /**
     * ????????????
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //??????csrf
                .csrf().disable()
                //?????????Session??????SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // ??????????????????URL
        Set<String> ignoreAuth = securityProperties().getIgnoreAuth();
        ignoreAuth.add(securityProperties().getAuthUrl());
        String[] stringArray = new String[ignoreAuth.size()];
        String[] ignoreAuthArray = ignoreAuth.toArray(stringArray);
        http.authorizeRequests()
                // ??????????????????
                .antMatchers(ignoreAuthArray).anonymous()
                // ???????????????????????????????????????????????????
                .anyRequest().authenticated();

        // spring security??????X-Frame-Options???????????????Frame ???????????????
        http.headers().frameOptions().disable();

        // ?????????????????????????????????????????????
        http.logout().disable();

        //?????????????????????
        http.exceptionHandling()
                //???????????????????????????
                .authenticationEntryPoint(authenticationEntryPoint())
                //???????????????????????????
                .accessDeniedHandler(accessDeniedHandler());

        //???????????????
        http.addFilterBefore(integrationAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter(), IntegrationAuthenticationFilter.class);

        //????????????
        http.cors();
    }

}
