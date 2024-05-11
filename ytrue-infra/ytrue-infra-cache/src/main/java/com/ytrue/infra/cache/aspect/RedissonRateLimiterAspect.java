package com.ytrue.infra.cache.aspect;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.cache.annotation.RedissonRateLimiter;
import com.ytrue.infra.cache.excptions.RedissonRateLimiterException;
import com.ytrue.infra.cache.util.SpelParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author ytrue
 * @date 2023-12-04 16:30
 * @description RedissonRateLimiterAspect
 */
@Aspect
@Order(-10)
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonRateLimiterAspect {

    private final RedissonClient redissonClient;


    @Pointcut("@annotation(limiter)")
    public void redissonRateLimiterAspect(RedissonRateLimiter limiter) {
    }

    @Around(value = "redissonRateLimiterAspect(limiter)", argNames = "proceedingJoinPoint,limiter")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint, RedissonRateLimiter limiter) throws Throwable {
        // 获取方法上面相关的
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = proceedingJoinPoint.getArgs();

        // 获取注解上面的
        // 获取value名称
        String redisKeyName = limiter.value();
        StringBuilder changeAfterRedisKeyName = new StringBuilder(redisKeyName);
        String[] keys = limiter.keys();
        // value:xxx:zzzz
        for (String key : keys) {
            String parseStr = SpelParserUtil.parse(method, args, key, String.class);
            if (StrUtil.isNotBlank(parseStr)) {
                changeAfterRedisKeyName.append(":").append(parseStr);
            }
        }

        long rate = limiter.rate();
        long rateInterval = limiter.rateInterval();
        RateIntervalUnit rateIntervalUnit = limiter.timeUnit();
        String failMessage = limiter.failMessage();
        //声明一个限流器
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(changeAfterRedisKeyName.toString());
        //设置速率 RateType.OVERALL所有实例共享、RateType.PER_CLIENT单实例端共享
        rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, rateIntervalUnit);
        //防止占redis内存，设置过期时间
        rateLimiter.expireAsync(Duration.ofMillis(rateIntervalUnit.toMillis(rateInterval)));
        //试图获取一个令牌，成功返回true
        boolean res = rateLimiter.tryAcquire();
        if (res) {
            return proceedingJoinPoint.proceed();
        }
        // 失败这里给提示
        throw new RedissonRateLimiterException(failMessage);
    }
}
