package com.ytrue.infra.cache.config;

import com.ytrue.infra.cache.ser.Jackson2JsonRedisSerializer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author ytrue
 * @date 2021/9/30 14:55
 * @description RedisConfig
 */
@Slf4j
@Configuration
public class RedisConfig {

    @Resource
    private RedisConnectionFactory connectionFactory;

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {

        //使用fastjson序列化,使用这个获取的value是JSONObject
        //FastJsonRedisSerializer<Object> jsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // 获取这个，这是是处理了LocalDateTime
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = Jackson2JsonRedisSerializer.genericJackson2JsonRedisSerializer();
        return getStringObjectRedisTemplate(genericJackson2JsonRedisSerializer);
    }

    private RedisTemplate<String, Object> getStringObjectRedisTemplate(GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 设置key
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        // hash key
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hash value
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

}
