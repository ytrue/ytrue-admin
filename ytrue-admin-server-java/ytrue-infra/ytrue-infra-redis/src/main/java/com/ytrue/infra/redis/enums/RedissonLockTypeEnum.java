package com.ytrue.infra.redis.enums;

/**
 * @author ytrue
 * @date 2023-11-28 15:40
 * @description 锁的模式
 */
public enum RedissonLockTypeEnum {

    //可重入锁
    REENTRANT,
    //公平锁
    FAIR,
    //联锁
    MULTIPLE,
    //红锁
    REDLOCK,
    //读锁
    READ,
    //写锁
    WRITE,
    //自动模式,当参数只有一个.使用 REENTRANT 参数多个 REDLOCK
    AUTO
}
