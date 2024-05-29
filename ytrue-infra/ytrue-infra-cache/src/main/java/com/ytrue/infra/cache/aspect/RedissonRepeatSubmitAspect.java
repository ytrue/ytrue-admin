package com.ytrue.infra.cache.aspect;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.cache.annotation.RedissonRepeatSubmit;
import com.ytrue.infra.cache.excptions.RedissonRepeatSubmitException;
import com.ytrue.infra.cache.util.SpelParserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2023-12-04 17:05
 * @description RedissonRepeatSubmitAspect
 */
@Aspect
@Order(-9)
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonRepeatSubmitAspect {

    private final RedissonClient redissonClient;


    @Pointcut("@annotation(repeatSubmit)")
    public void redissonRepeatSubmitAspect(RedissonRepeatSubmit repeatSubmit) {
    }


    @Around(value = "redissonRepeatSubmitAspect(repeatSubmit)", argNames = "proceedingJoinPoint,repeatSubmit")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint, RedissonRepeatSubmit repeatSubmit) throws Throwable {

        // 获取方法上面相关的
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = proceedingJoinPoint.getArgs();


        // 获取注解上面的
        String redisKeyName = repeatSubmit.value();
        StringBuilder changeAfterRedisKeyName = new StringBuilder(redisKeyName);
        String[] keys = repeatSubmit.keys();
        Arrays.stream(keys).forEach(key -> {
            String parseStr = SpelParserUtil.parse(method, args, key, String.class);
            if (StrUtil.isNotBlank(parseStr)) {
                changeAfterRedisKeyName.append(":").append(parseStr);
            }
        });

        long expireTime = repeatSubmit.expireTime();
        String failMessage = repeatSubmit.failMessage();
        TimeUnit timeUnit = repeatSubmit.timeUnit();
        boolean waitExpire = repeatSubmit.waitExpire();
        boolean validateForm = repeatSubmit.validateForm();


        if (validateForm) {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            Map<String, String[]> parameterMap = request.getParameterMap();
            // execution(权限标识 @RedissonRepeatSubmit标注的全限定类名.@RedissonRepeatSubmit方法名)
            String classNameAndMethodName = proceedingJoinPoint.getStaticPart().toLongString();
            // MD5哈希值
            String pointHex = DigestUtils.md5DigestAsHex(classNameAndMethodName.getBytes());
            //请求的表单参数 生成hex 作为签名参数
            StringBuilder signature = new StringBuilder(pointHex);
            // name=123,3232
            parameterMap.forEach((key, values) -> {
                String tmp = key + "=" + String.join(",", values);
                signature.append(DigestUtils.md5DigestAsHex(tmp.getBytes()));
            });
            // 那这个拼接
            changeAfterRedisKeyName.append(":").append(DigestUtils.md5DigestAsHex(signature.toString().getBytes()));
        }

        RLock rLock = redissonClient.getLock(changeAfterRedisKeyName.toString());
        try {
            if (rLock.tryLock(0, expireTime, timeUnit)) {
                return proceedingJoinPoint.proceed();
            } else {
                throw new RedissonRepeatSubmitException(failMessage);
            }
        } finally {
            // 解锁
            if (!waitExpire) {
                // 有锁才删除
                if (rLock.isLocked()) {
                    rLock.unlock();
                }else {
                    log.warn("防重复提前过期了，无需删除");
                }
            }
        }
    }

}
