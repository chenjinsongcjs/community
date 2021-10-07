package com.nowcoder.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nowcoder.community.constant.TicketStatusConstant;
import com.nowcoder.community.dao.LoginTicketDao;
import com.nowcoder.community.domain.LoginTicket;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.LogoutService;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/0:36
 * @Description:
 */
@Service
public class LogoutServiceImpl implements LogoutService {
//    @Autowired
//    private LoginTicketDao loginTicketDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void logout(String ticket) {
//        loginTicketDao.updateStatusByTicket(ticket, TicketStatusConstant.TICKET_STATUS_INVALID.getCode());
        User user = LoginInterceptor.users.get();
        String loginTicketKey = RedisKeyUtils.getLoginTicketKey(ticket);
        String loginTicket = redisTemplate.opsForValue().get(loginTicketKey);
        LoginTicket loginTicket1 = JSONObject.parseObject(loginTicket, new TypeReference<LoginTicket>() {
        });
        loginTicket1.setStatus(TicketStatusConstant.TICKET_STATUS_INVALID.getCode());
        redisTemplate.opsForValue().set(loginTicketKey,JSONObject.toJSONString(loginTicket1));
    }
}