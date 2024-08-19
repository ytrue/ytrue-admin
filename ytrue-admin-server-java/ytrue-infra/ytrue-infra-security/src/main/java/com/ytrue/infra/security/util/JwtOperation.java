package com.ytrue.infra.security.util;

import com.ytrue.infra.security.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author ytrue
 * @date 2023-06-28 8:57
 * @description JwtUtil
 */
@Component
@Data
@Slf4j
public class JwtOperation {
    /**
     * Token过期时间必须大于生效时间
     */
    private Long tokenExpireTime;

    /**
     * Token加密解密的密码
     */
    private String tokenSecret;

    /**
     * 加密类型 三个值可取 HS256  HS384  HS512
     */
    private MacAlgorithm jwtAlg;

    /**
     * 添加一个前缀
     */
    private String jwtSeparator;

    /**
     * 发布时间
     * 默认：new Date(System.currentTimeMillis() + START_TIME)
     */
    private Long startTime;

    /**
     * token在什么时间之前是不可用的（默认从当前时间）
     * 默认：new Date(System.currentTimeMillis() + BEFORE_TIME)
     */
    private Long beforeTime;

    @Resource
    public void setSecretKey(JwtProperties jwtProperties) {
        tokenExpireTime = jwtProperties.getTokenExpireTime();
        beforeTime = jwtProperties.getBeforeTime();
        startTime = jwtProperties.getStartTime();
        tokenSecret = jwtProperties.getTokenSecret();
        jwtSeparator = jwtProperties.getJwtSeparator();


        //转成大写判断
        jwtAlg = switch (jwtProperties.getJwtAlg().toUpperCase()) {
            case "HS256" -> Jwts.SIG.HS256;
            case "HS384" -> Jwts.SIG.HS384;
            default -> Jwts.SIG.HS512;
        };
    }


    /**
     * 根据负载生成jwt token
     *
     * @param claims
     * @return
     */
    public String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                // 设置自定义载体
                .claims(claims)
                // 设置token在什么时间之前是不可用的（默认从当前时间）
                .notBefore(new Date(System.currentTimeMillis() + beforeTime))
                // 设置token生效时间(默认是从当前开始生效)
                .issuedAt(new Date(System.currentTimeMillis() + startTime))
                // 设置Token过期时间必须大于生效时间
                .expiration(new Date(System.currentTimeMillis() + tokenExpireTime))
                // 设置签名加密方式
                .signWith(generateKey(), jwtAlg)
                .compact();


        return jwtSeparator + token;
    }


    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @param aud 接收token的一方
     * @param jti token的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @param iss token签发者
     * @param map 自定义信息的存储
     * @return 加密后的token字符串
     */
    public String createToken(String sub, String aud, String jti, String iss, Map<String, Object> map) {
        final JwtBuilder builder = Jwts.builder();

        if (Objects.nonNull(map)) {
            if (!map.isEmpty()) {
                builder.claims(map);
            }
        }
        String token = Jwts.builder()
                // 设置主题
                .subject(sub)
                // 设置 受众(给谁用的)比如：http://www.xxx.com
                .audience().add(aud).and()
                // 设置编号，JWT 的唯一身份标识
                .id(jti)
                // 设置 签发人（谁签发的）
                .issuer(iss)
                // 设置token在什么时间之前是不可用的（默认从当前时间）
                .notBefore(new Date(System.currentTimeMillis() + beforeTime))
                // 设置token生效时间(默认是从当前开始生效)
                .issuedAt(new Date(System.currentTimeMillis() + startTime))
                // 设置Token过期时间必须大于生效时间
                .expiration(new Date(System.currentTimeMillis() + tokenExpireTime))
                // 设置签名加密方式
                .signWith(generateKey(), jwtAlg)
                .compact();
        return jwtSeparator + token;

    }


    /**
     * 判断token是否有效
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        //判断token是否过期，判断token是否和userDetails中的username一致
        Claims claims = parseToken(token);
        String username = getValue(claims, token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    /**
     * 验证token是否有效
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        //claims 为null 意味着要门jwt被修改
        Claims claims = parseToken(token);
        return claims != null && !isExpired(token);
    }


    /**
     * 判断token、是否过期失效
     *
     * @param token
     * @return
     */
    public boolean isExpired(String token) {
        Date expiredDate = getExpiredDate(token);
        return expiredDate.before(new Date());
    }

    /**
     * 根据token获取失效时间
     * 也是先从token中获取荷载
     * 然后从荷载中拿到到设置的失效时间
     *
     * @param token
     * @return
     */
    public Date getExpiredDate(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 获取去掉前缀的token
     *
     * @param token
     * @return
     */
    public String getOriginalToken(String token) {
        // 移除 token 前的"Bearer#"字符串
        int pos = token.indexOf(jwtSeparator);
        token = pos == -1 ? "" : token.substring(pos + jwtSeparator.length());

        return token;
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public String getValue(Claims claims, String key) {
        return claims.get(key) != null ? claims.get(key).toString() : null;
    }


    /**
     * 刷新我们的token：重新构建jwt
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        return createToken(claims);
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    /**
     * 生产key
     *
     * @return
     */
    private SecretKey generateKey() {
        // 将将密码转换为字节数组
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(tokenSecret);
        // 根据指定的加密方式，生成密钥
        return Keys.hmacShaKeyFor(bytes);
    }
}

