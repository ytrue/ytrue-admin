package com.ytrue.infra.security.filter;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.ytrue.infra.security.excptions.InvalidTokenException;
import com.ytrue.infra.security.properties.SecurityProperties;
import com.ytrue.infra.security.user.LoginUser;
import com.ytrue.infra.security.user.User;
import com.ytrue.infra.security.util.JwtOperation;
import com.ytrue.infra.security.util.ServletWebRequestUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author ytrue
 * @date 2022/5/5 10:59
 * @description 自定义一个过滤器，这个过滤器会去获取请求头中的token，对token进行解析取出其中的userId
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    private final JwtOperation jwtOperation;

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 获取token
        String token = request.getHeader(securityProperties.getAuthorizationHeaderParameterName());

        //如果header没有token就是直接放行
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取去掉前缀的token
        token = jwtOperation.getOriginalToken(token);

        String userId;
        try {
            // 获取jwt的载体
            Claims claims = jwtOperation.parseToken(token);
            userId = jwtOperation.getValue(claims, "userId");

            // 再次验证一下
            if (StrUtil.isBlank(userId)) {
                throw new InvalidTokenException("无效token");
            }
        } catch (Exception e) {
            ServletWebRequestUtil.errorPathForward(request, response, e, 4001);
            return;
        }

        String userJsonData = redisTemplate.opsForValue().get(securityProperties.getTokenCachePrefix() + userId);
        if (StrUtil.isEmpty(userJsonData)) {
            ServletWebRequestUtil.errorPathForward(request, response, new InvalidTokenException("无效token"), 4001);
            return;
        }

        User user = new Gson().fromJson(userJsonData, User.class);

        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, loginUser.getPassword(), loginUser.getAuthorities());

        //存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
