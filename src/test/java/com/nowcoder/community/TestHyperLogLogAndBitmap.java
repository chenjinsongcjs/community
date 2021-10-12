package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/23:28
 * @Description:
 */
@SpringBootTest
public class TestHyperLogLogAndBitmap {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Test
    void testHyperLogLog(){
        String redisKey01= "test:hll:01";
        for (int i = 1; i <= 100 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey01,String.valueOf(i));
        }
        String redisKey02 = "test:hll:02";
        for (int i = 50; i <= 150 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey02,String.valueOf(i));
        }
        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey,redisKey01,redisKey02);
        System.out.println(redisTemplate.opsForHyperLogLog().size(unionKey));
    }
    @Test
    void testBitMap(){
        String redisKey = "test:bm";
        for (int i = 1; i < 100 ; i++) {
            redisTemplate.opsForValue().setBit(redisKey,i,true);
        }
        Object num = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Long aLong = connection.bitCount(redisKey.getBytes());
                return aLong;
            }
        });
        System.out.println(num);
    }
}