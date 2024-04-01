package com.ytrue.infra.core.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ytrue
 * @date 2023-11-22 9:58
 * @description SnowFlakeInitiator
 */
@Component
@Slf4j
public class SnowFlakeInitiator {


    /**
     * 缓存前缀
     */
    private static final String KEY = "SNOWFLAKE_ID";

    private static final int MULTIPLE = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @PostConstruct
    public void init() {


        Long num = stringRedisTemplate.opsForValue().increment(KEY);

        assert num != null;

        long dataCenter = num / MULTIPLE;
        long workedId = num % MULTIPLE;
        //如果数据中心大于32，则抹除缓存，从头开始
        if (dataCenter >= MULTIPLE) {
            stringRedisTemplate.delete(KEY);
            num = stringRedisTemplate.opsForValue().increment(KEY);
            dataCenter = num / MULTIPLE;
            workedId = num % MULTIPLE;
        }
        SnowFlake.initialize(workedId, dataCenter);
    }


}
