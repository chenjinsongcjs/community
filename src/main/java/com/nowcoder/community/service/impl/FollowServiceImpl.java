package com.nowcoder.community.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.RedisKeyUtils;
import com.nowcoder.community.vo.FollowPageVo;
import com.nowcoder.community.vo.FollowUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/21:56
 * @Description:
 */
@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;


    @Override
    public void follow(int entityType, int entityId) {
        User user = LoginInterceptor.users.get();
        if (user == null)
            throw new RuntimeException("用户未登录");
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                String followeeKey = RedisKeyUtils.getFolloweeKey(entityType, entityId);
                String followerKey = RedisKeyUtils.getFollowerKey(user.getId(), entityType);
                ops.multi();//开启事务
                //以当前用户为主体
                //我是被关注者，那些人关注了我
                redisTemplate.opsForZSet().add(followeeKey,user.getId()+"",System.currentTimeMillis());
                //我是那些对象的粉丝
                redisTemplate.opsForZSet().add(followerKey,entityId+"",System.currentTimeMillis());
                return ops.exec();
            }
        });
    }

    @Override
    public void unfollow(int entityType, int entityId) {
        User user = LoginInterceptor.users.get();
        if (user == null)
            throw new RuntimeException("用户未登录");
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                String followeeKey = RedisKeyUtils.getFolloweeKey(entityType, entityId);
                String followerKey = RedisKeyUtils.getFollowerKey(user.getId(), entityType);
                ops.multi();//开启事务
                //以当前用户为主体
                //我是被关注者，那些人关注了我
                redisTemplate.opsForZSet().remove(followeeKey,user.getId()+"");
                //我是那些对象的粉丝
                redisTemplate.opsForZSet().remove(followerKey,entityId+"");
                return ops.exec();
            }
        });
    }

    @Override
    public FollowPageVo getFolloweeOfMine(int entityType,int userId, int pageNum, int pageSize) {
        String followerKey = RedisKeyUtils.getFollowerKey(userId, entityType);
        //注意redis的索引是从零开始的
        Set<String> entityIds = redisTemplate.opsForZSet().reverseRange(followerKey, pageNum-1, pageNum + pageSize - 1);
        if(entityIds != null){
            List<Integer> ids = entityIds.stream().map(Integer::parseInt).collect(Collectors.toList());
            Page<User> userPage = PageHelper.startPage(pageNum, pageSize);
            if (ids.size() > 0){
                List<User> users = userService.getUserByIdBatch(ids);
                List<FollowUserVo> followUserVos = users.stream().map(user1 -> {
                    FollowUserVo followUserVo = new FollowUserVo();
                    followUserVo.setUser(user1);
                    Double score = redisTemplate.opsForZSet().score(followerKey, user1.getId()+"");
                    if (score != null)
                        followUserVo.setFollowTime(new Date(score.longValue()));
                    followUserVo.setFollowStatus(followStatus(entityType, user1.getId()));
                    return followUserVo;
                }).collect(Collectors.toList());
                PageInfo<User> userPageInfo = new PageInfo<>(userPage, PageConstant.NAVIGATE_PAGES);
                return new FollowPageVo(userPageInfo,followUserVos);
            }

        }


        return null;
    }

    @Override
    public FollowPageVo getFollowerOfMine(int entityType,int userId,int pageNum,int pageSize) {

        String followeeKey = RedisKeyUtils.getFolloweeKey(entityType, userId);
        Set<String> userIds = redisTemplate.opsForZSet().reverseRange(followeeKey, pageNum-1, pageNum + pageSize - 1);
        if(userIds != null){
            List<Integer> ids = userIds.stream().map(Integer::parseInt).collect(Collectors.toList());
            Page<User> userPage = PageHelper.startPage(pageNum, pageSize);
            if(ids.size() > 0){
                List<User> users = userService.getUserByIdBatch(ids);
                List<FollowUserVo> followUserVos = users.stream().map(user1 -> {
                    FollowUserVo followUserVo = new FollowUserVo();
                    followUserVo.setUser(user1);
                    Double score = redisTemplate.opsForZSet().score(followeeKey, user1.getId()+"");
                    if (score != null)
                        followUserVo.setFollowTime(new Date(score.longValue()));
                    followUserVo.setFollowStatus(followStatus(entityType, user1.getId()));
                    return followUserVo;
                }).collect(Collectors.toList());
                PageInfo<User> userPageInfo = new PageInfo<>(userPage, PageConstant.NAVIGATE_PAGES);
                return new FollowPageVo(userPageInfo,followUserVos);
            }

        }

        return null;
    }

    @Override
    public boolean followStatus(int entityType, int entityId) {
        User user = LoginInterceptor.users.get();
        if(user == null)
            throw new RuntimeException("用户未登录");
        String followerKey = RedisKeyUtils.getFollowerKey(user.getId(), entityType);
        Double score = redisTemplate.opsForZSet().score(followerKey, entityId+"");
        return score != null;
    }

    @Override
    public long followerCount(int entityType,int userId) {
        String followeeKey = RedisKeyUtils.getFolloweeKey(entityType, userId);
        Long aLong = redisTemplate.opsForZSet().zCard(followeeKey);
        return aLong == null ?0 : aLong;
    }

    @Override
    public long followeeCount(int entityType,int userId) {
        String followerKey = RedisKeyUtils.getFollowerKey(userId, entityType);
        Long aLong = redisTemplate.opsForZSet().zCard(followerKey);
        return aLong == null ? 0:aLong;
    }
}