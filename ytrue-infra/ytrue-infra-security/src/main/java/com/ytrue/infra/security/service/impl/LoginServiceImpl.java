package com.ytrue.infra.security.service.impl;

import com.google.gson.Gson;
import com.ytrue.infra.security.properties.JwtProperties;
import com.ytrue.infra.security.properties.SecurityProperties;
import com.ytrue.infra.security.service.LoginService;
import com.ytrue.infra.security.user.LoginUser;
import com.ytrue.infra.security.user.User;
import com.ytrue.infra.security.util.JwtOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2022/4/26 11:04
 * @description LoginServiceImpl
 */
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    private final StringRedisTemplate redisTemplate;

    private final JwtOperation jwtOperation;

    private final SecurityProperties securityProperties;

    private final JwtProperties jwtProperties;

    @Override
    public Map<String, String> login() {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null);


        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        LoginUser loginUser = ((LoginUser) authenticate.getPrincipal());
        User user1 = loginUser.getUser();
        String userId = user1.getUserId();

        //加入缓存
        redisTemplate.opsForValue().set(
                securityProperties.getTokenCachePrefix() + userId,
                new Gson().toJson(user1),
                jwtProperties.getTokenExpireTime(),
                TimeUnit.MILLISECONDS
        );

        //把token响应给前端，使用map是未来后期扩展，这里还会加入过期事件，刷新token等...
        HashMap<String, String> resultMap = new HashMap<>(2);

        // 自定义载体
        HashMap<String, Object> claimsMap = new HashMap<>(2);
        claimsMap.put("userId", userId);

        resultMap.put("token", jwtOperation.createToken(claimsMap));
        resultMap.put("expireTime", jwtProperties.getTokenExpireTime().toString());
        return resultMap;
    }

    /**
     * 退出登录
     */
    @Override
    public void logout(String userId) {
        redisTemplate.delete(securityProperties.getTokenCachePrefix() + userId);
    }
}
