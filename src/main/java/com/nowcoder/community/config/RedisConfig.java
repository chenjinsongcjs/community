package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/6:08
 * @Description:
 */
@Configuration
public class RedisConfig {
    /**
    * @Description: 改变redis的序列化器为json的
    * @Param: []
    * @return: []
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    @Bean
    public RedisSerializer redisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}