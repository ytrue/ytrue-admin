package com.ytrue.security;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.infra.cache.constant.CacheKey;
import com.ytrue.infra.core.excptions.LoginFailureException;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.service.system.SysPermissionService;
import com.ytrue.service.system.SysUserService;
import com.ytrue.tools.security.integration.IntegrationAuthenticationEntity;
import com.ytrue.tools.security.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import com.ytrue.tools.security.user.LoginUser;
import com.ytrue.tools.security.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author ytrue
 * @description: 用户名和密码认证
 * @date 2022/12/8 9:10
 */
@Component
@AllArgsConstructor
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    private final SysPermissionService sysPermissionService;

    private final SysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    private final StringRedisTemplate redisTemplate;

    private final static String AUTH_TYPE = "password";

    @Override
    public LoginUser authenticate(IntegrationAuthenticationEntity entity) {
        // 获取用户名称 password ， username
        String username = entity.getAuthParameter("username");
        String password = entity.getAuthParameter("password");
        // 获取验证码
        String code = entity.getAuthParameter("code");
        String uuid = entity.getAuthParameter("uuid");

        // 校验空
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            throw new LoginFailureException("账号或者密码不正确");
        }

        // 校验uid
        if (StrUtil.isEmpty(uuid)) {
            // 非法操作
            throw new LoginFailureException("非法操作");
        }

        // 校验验证码
        String captchaValue = redisTemplate.opsForValue().get(CacheKey.ADMIN_LOGIN_CAPTCHA + uuid);
        if (StrUtil.isEmpty(captchaValue) || !captchaValue.equals(code)) {
            // 验证码错误
            throw new LoginFailureException("验证码错误");
        }

        SysUser sysUser = sysUserService.getOne(
                Wrappers.<SysUser>lambdaQuery()
                        .eq(SysUser::getUsername, username)
        );

        // 校验用户名是否存在
        AssertUtil.notNull(sysUser, ServerResponseCode.error("账号不存在"));
        // 校验密码
        AssertUtil.isTrue(passwordEncoder.matches(password, sysUser.getPassword()), ServerResponseCode.error("账号或者密码不正确"));

        User user = new User();
        user.setUserId(Convert.toStr(sysUser.getId()));
        user.setUsername(sysUser.getUsername());
        user.setPassword(sysUser.getPassword());
        user.setAuthorities(sysPermissionService.getPermission(sysUser));
        user.setRoles(sysPermissionService.getRoleCode(sysUser));
        // 设置邮箱
        HashMap<String, String> extend = new HashMap<>(2);
        extend.put("email", sysUser.getEmail());
        // 登录类型
        extend.put("type", "MANAGER");
        user.setExtend(extend);

        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        return loginUser;
    }

    @Override
    public boolean support(IntegrationAuthenticationEntity entity) {
        String authType = entity.getAuthType();
        // 等于password或者为空都是走密码
        return AUTH_TYPE.equals(authType) || StrUtil.isEmpty(authType);
    }
}
