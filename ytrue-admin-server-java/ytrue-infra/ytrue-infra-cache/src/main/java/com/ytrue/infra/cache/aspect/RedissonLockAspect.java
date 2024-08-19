package com.ytrue.infra.cache.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.cache.annotation.RedissonLock;
import com.ytrue.infra.cache.enums.RedissonLockTypeEnum;
import com.ytrue.infra.cache.excptions.RedissonLockFailureException;
import com.ytrue.infra.cache.strategy.RedissonLockFailureStrategy;
import com.ytrue.infra.cache.util.SpelParserUtil;
import lombok.RequiredArgsConstructor;
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
import org.springframework.beans.BeanUtils;
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
@RequiredArgsConstructor
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
        // 获取策略
        RedissonLockFailureStrategy failureStrategy = BeanUtils.getResolvableConstructor(lock.failureStrategy()).newInstance();


        log.info("锁模式->{},等待锁定时间->{}.锁定最长时间->{} 时间单位->{}", lockType.name(), waitTime, leaseTime, timeUnit.name());

        boolean res = false;
        RLock rLock = switch (lockType) {
            case FAIR -> redissonClient.getFairLock(changeAfterRedisKeyName.toString());
            case REENTRANT -> redissonClient.getLock(changeAfterRedisKeyName.toString());

            case READ -> redissonClient.getReadWriteLock(changeAfterRedisKeyName.toString()).readLock();
            case WRITE -> redissonClient.getReadWriteLock(changeAfterRedisKeyName.toString()).writeLock();

            case MULTIPLE -> new RedissonMultiLock(getLocks(parseStrArr, redisKeyName));
            case REDLOCK -> new RedissonRedLock(getLocks(parseStrArr, redisKeyName));
            default -> throw new RedissonLockFailureException("lock类型错误");
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
                }

                // 按照失败策略处理
                failureStrategy.onFailure(lock, method, args);
                // 下面还是继续执行内容，是不是放下面执行是由failureStrategy处理的，你可以直接抛出异常不往下面处理
                return proceedingJoinPoint.proceed();
            } finally {
                if (res) {
                    // 有锁才删除
                    if (rLock.isLocked()) {
                        rLock.unlock();
                    } else {
                        log.warn("锁提前过期了，无需删除");
                    }
                }
            }
        }
        failureStrategy.onFailure(lock, method, args);
        return proceedingJoinPoint.proceed();
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
        if (CollUtil.isEmpty(lockList)) {
            RLock lock = redissonClient.getLock(redisKeyName);
            lockList.add(lock);
        }

        return lockList.toArray(new RLock[0]);
    }
}
