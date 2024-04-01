package com.ytrue.infra.cache.aspect;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.cache.annotation.RedissonLock;
import com.ytrue.infra.cache.enums.RedissonLockTypeEnum;
import com.ytrue.infra.cache.excptions.RedissonLockException;
import com.ytrue.infra.core.util.SpelParserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2023-11-28 15:48
 * @description RedissonLockAspect
 */
@Aspect
@Order(-15)
@Slf4j
@Component
@AllArgsConstructor
public class RedissonLockAspect {
    private final RedissonClient redissonClient;


    @Pointcut("@annotation(lock)")
    public void redissonLockAspect(RedissonLock lock) {
    }


    @Around(value = "redissonLockAspect(lock)", argNames = "proceedingJoinPoint,lock")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint, RedissonLock lock) throws Throwable {
        // 获取方法上面相关的
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = proceedingJoinPoint.getArgs();

        // 获取注解上面的
        // 获取value名称
        String redisKeyName = lock.value();
        StringBuilder changeAfterRedisKeyName = new StringBuilder(redisKeyName);
        RedissonLockTypeEnum lockType = lock.type();

        String[] keys = lock.keys();
        String[] parseStrArr = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            String parseStr = SpelParserUtil.parse(method, args, keys[i], String.class);
            if (StrUtil.isNotBlank(parseStr)) {
                changeAfterRedisKeyName.append(":").append(parseStr);
                // 加入进去
                parseStrArr[i] = parseStr;
            }
        }

        long waitTime = lock.waitTime();
        long leaseTime = lock.leaseTime();
        TimeUnit timeUnit = lock.timeUnit();
        String failMessage = lock.failMessage();

        log.info("锁模式->{},等待锁定时间->{}.锁定最长时间->{} 时间单位->{}", lockType.name(), waitTime, leaseTime, timeUnit.name());

        boolean res = false;
        RLock rLock = switch (lockType) {
            case FAIR -> redissonClient.getFairLock(changeAfterRedisKeyName.toString());
            case REENTRANT -> redissonClient.getLock(changeAfterRedisKeyName.toString());

            case READ -> redissonClient.getReadWriteLock(changeAfterRedisKeyName.toString()).readLock();
            case WRITE -> redissonClient.getReadWriteLock(changeAfterRedisKeyName.toString()).writeLock();

            case MULTIPLE -> new RedissonMultiLock(getLocks(parseStrArr, redisKeyName));
            case REDLOCK -> new RedissonRedLock(getLocks(parseStrArr, redisKeyName));
            default -> throw new RedissonLockException("lock类型错误");
        };

        //一直等待加锁.
        //执行aop
        if (rLock != null) {
            try {
                if (waitTime == -1) {
                    res = true;
                    // 10秒钟自动解锁,自动解锁时间一定要大于业务执行时间
                    rLock.lock(leaseTime, timeUnit);
                } else {
                    // 最多等待100秒，上锁以后10秒自动解锁
                    res = rLock.tryLock(waitTime, leaseTime, timeUnit);
                }
                if (res) {
                    return proceedingJoinPoint.proceed();
                } else {
                    throw new RedissonLockException(failMessage);
                }
            } finally {
                if (res) {
                    rLock.unlock();
                }
            }
        }
        throw new RedissonLockException(failMessage);
    }


    private RLock[] getLocks(String[] parseStrArr, String redisKeyName) {
        List<RLock> lockList = new ArrayList<>();

        for (String s : parseStrArr) {
            // 解析出来的数据不能是空白字符或者空
            if (StrUtil.isNotBlank(s)) {
                RLock lock = redissonClient.getLock(redisKeyName + ":" + s);
                lockList.add(lock);
            }
        }

        // 如果没有，这里直接以为redisKeyName 作为key了
        if (lockList.size() == 0) {
            RLock lock = redissonClient.getLock(redisKeyName);
            lockList.add(lock);
        }

        return lockList.toArray(new RLock[0]);
    }
}
