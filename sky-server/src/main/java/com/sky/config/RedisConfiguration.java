package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("redis");
        RedisTemplate redisTemplate=new RedisTemplate();
        //设置redis的连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //序列化器 不加在redis图形界面展示的时候会乱码
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
