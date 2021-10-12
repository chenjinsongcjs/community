package com.nowcoder.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/8:23
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public User getUserById(int id) {
        User user = getCache(id);
        if (user == null)
            return initCache(id);
        return user;
    }

    @Override
    public List<User> getUserByIdBatch(List<Integer> ids) {
        List<User> retVal = new ArrayList<>();
        for (Integer id : ids) {
            User user = getCache(id);
            if(user == null)
                user = initCache(id);
            retVal.add(user);
        }
        return retVal;
    }

    @Override
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public int saveUser(User user) {
        return userDao.saveUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int updateUserHeaderImage(int userId, String path) {
        return userDao.updateUserHeaderImage(userId,path);
    }

    //先从缓存中查找数据
    private User getCache(int userId){
        String userKey = RedisKeyUtils.getUserKey(userId);
        String userStr = redisTemplate.opsForValue().get(userKey);
        User user = JSONObject.parseObject(userStr, new TypeReference<User>() {
        });
        return user;
    }
    //初始化缓存中的数据
    private User initCache(int userId){
        User user = userDao.getUserById(userId);
        String userKey = RedisKeyUtils.getUserKey(userId);
        redisTemplate.opsForValue().set(userKey,JSONObject.toJSONString(user));
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.getUserByName(s);
    }
}