package com.ytrue.security;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.model.po.SysUser;
import com.ytrue.modules.system.service.impl.SysPermissionServiceImpl;
import com.ytrue.tools.security.integration.IntegrationAuthenticationEntity;
import com.ytrue.tools.security.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import com.ytrue.tools.security.user.LoginUser;
import com.ytrue.tools.security.user.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author ytrue
 * @description: 用户名和密码认证
 * @date 2022/12/8 9:10
 */
@Component
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {


    @Resource
    private SysPermissionServiceImpl sysPermissionService;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private PasswordEncoder passwordEncoder;

    private final static String AUTH_TYPE = "password";

    @Override
    public LoginUser authenticate(IntegrationAuthenticationEntity entity) {
        // 获取用户名称 password ， username
        String username = entity.getAuthParameter("username");
        String password = entity.getAuthParameter("password");
        // 校验空
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            throw new RuntimeException("用户名或者密码不正确!");
        }
        // 校验用户名是否存在
        SysUser sysUser = sysUserDao.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));

        if (sysUser == null) {
            throw new RuntimeException("登录用户：" + username + " 不存在");
        }

        // 校验密码
        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        User user = new User();
        user.setUserId(Convert.toStr(sysUser.getId()));
        user.setUsername(sysUser.getUsername());
        user.setPassword(sysUser.getPassword());
        user.setAuthorities(sysPermissionService.getPermission(sysUser));

        // 设置邮箱
        HashMap<String, String> extend = new HashMap<>(16);
        extend.put("email", sysUser.getEmail());
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
