package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.StatisticalService;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/0:24
 * @Description:
 */
@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private  final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void addUV(Date date, String ip) {
        String uvKey = RedisKeyUtils.getUVKey(format.format(date));
        redisTemplate.opsForHyperLogLog().add(uvKey,ip);
    }

    @Override
    public void addDAU(Date date, int userId) {
        String dauKey = RedisKeyUtils.getDAUKey(format.format(date));
        redisTemplate.opsForValue().setBit(dauKey,userId,true);
    }

    @Override
    public long intervalUV(Date start, Date end) {
        if (start == null || end == null)
            throw new RuntimeException("开始和结束日期不能为空");
        //用于存储区间数据
        String unionUVKey = RedisKeyUtils.getUnionUVKey(format.format(start), format.format(end));
        List<String> redisKeys = new ArrayList<>();
        //日期处理
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){
            String redisKey = RedisKeyUtils.getUVKey(format.format(calendar.getTime()));
            redisKeys.add(redisKey);
            //日期+1
            calendar.add(Calendar.DATE,1);
        }
        //聚合
        redisTemplate.opsForHyperLogLog().union(unionUVKey,redisKeys.toArray(new String[0]));
        return redisTemplate.opsForHyperLogLog().size(unionUVKey);
    }

    @Override
    public long intervalDAU(Date start, Date end) {
        if (start == null || end == null)
            throw new RuntimeException("开始和结束日期不能为空");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        List<byte[]> redisKeys = new ArrayList<>();
        while (!calendar.getTime().after(end)){
            String dauKey = RedisKeyUtils.getDAUKey(format.format(calendar.getTime()));
            redisKeys.add(dauKey.getBytes());
            calendar.add(Calendar.DATE,1);
        }
        String unionDAUKey = RedisKeyUtils.getUnionDAUKey(format.format(start), format.format(end));
        Object size = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                //做聚合
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        unionDAUKey.getBytes(),
                        redisKeys.toArray(new byte[0][0]));
                //统计数量
                return connection.bitCount(unionDAUKey.getBytes());
            }
        });
        return (long) size;
    }
}