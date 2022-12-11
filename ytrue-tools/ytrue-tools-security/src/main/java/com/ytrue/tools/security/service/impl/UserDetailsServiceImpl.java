package com.ytrue.tools.security.service.impl;

import com.ytrue.tools.security.integration.IntegrationAuthenticationContext;
import com.ytrue.tools.security.integration.IntegrationAuthenticationEntity;
import com.ytrue.tools.security.integration.authenticator.IntegrationAuthenticator;
import com.ytrue.tools.security.user.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ytrue
 * @date 2022/4/26 11:06
 * @description UserDetailsServiceImpl
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 认证集合
     */
    private List<IntegrationAuthenticator> authenticators;


    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //去获得上下文
        IntegrationAuthenticationEntity entity = IntegrationAuthenticationContext.get();
        //判断是否为空
        entity = entity == null ? new IntegrationAuthenticationEntity() : entity;

        //判断是否支持集成认证类型
        LoginUser loginUser = authenticate(entity);

        if (loginUser == null) {
            throw new InternalAuthenticationServiceException("认证服务错误");
        }
        return loginUser;
    }

    /**
     * 判断是否支持集成认证类型
     *
     * @param entity
     * @return
     */
    private LoginUser authenticate(IntegrationAuthenticationEntity entity) {
        if (this.authenticators == null) {
            throw new InternalAuthenticationServiceException("未定义认证服务！");
        }

        for (IntegrationAuthenticator authenticator : authenticators) {
            //是否支持该类型
            if (authenticator.support(entity)) {
                //执行方法
                return authenticator.authenticate(entity);
            }
        }
        throw new InternalAuthenticationServiceException("无效的auth_type参数值！");
    }
}
