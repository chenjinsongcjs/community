package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/6:06
 * @Description:
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void like(int entityType, int entityId, int userId, int entityUserId) {
        //对redis的两个键进行操作，要进行事务管理
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public  Object execute(RedisOperations ops) throws DataAccessException {
                String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
                String likeUserKey = RedisKeyUtils.getLikeUserKey(userId);
                //先判断是否点赞，没有点赞就点赞，点赞了就取消点赞 ,参数为字符串否则报错
                Boolean member = ops.opsForSet().isMember(likeKey,userId+"");
                ops.multi();
                //执行事务之内的操作
                if(member){//点赞了就取消
                    ops.opsForSet().remove(likeKey,userId+"");
                    ops.opsForValue().decrement(likeUserKey);
                }else{
                    //没有点赞，点赞
                    ops.opsForSet().add(likeKey,userId+"");
                    ops.opsForValue().increment(likeUserKey);
                }
                return ops.exec();
            }
        });
    }

    @Override
    public boolean likeStatus(int entityType, int entityId) {
        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeKey) != 0;
    }

    @Override
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeKey);
    }

    @Override
    public long getAllLikeCountOfUser(int userId) {
        String likeUserKey = RedisKeyUtils.getLikeUserKey(userId);
        return Long.parseLong(redisTemplate.opsForValue().get(likeUserKey));
    }
}