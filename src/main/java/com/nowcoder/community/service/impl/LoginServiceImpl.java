package com.nowcoder.community.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.constant.ActivationConstant;
import com.nowcoder.community.constant.TicketStatusConstant;
import com.nowcoder.community.domain.LoginTicket;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/22:47
 * @Description: 用户登录服务实现类，返回值用于返回一些错误提示信息
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserService userService;
//    @Autowired
//    private LoginTicketDao loginTicketDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Map<String, String> login(User user, int expiredSecond) {
        //校验用户名和密码是否合法 ： 后期可以同JSR303校验
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //检查用户是否存在或者激活
        User user1 = userService.getUserByName(user.getUsername());
        if (user1 == null){
            map.put("usernameMsg","用户不存在");
            return map;
        }
        if (user1.getStatus() == ActivationConstant.ACTIVATION_STATUS_0){
            map.put("usernameMsg","用户未激活");
            return map;
        }
        //密码验证
        String password = DigestUtil.md5Hex(user.getPassword()+user1.getSalt());
        if(!password.equals(user1.getPassword())){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        //设置登录令牌
        String ticket = IdUtil.simpleUUID();
        LoginTicket loginTicket = new LoginTicket(null,user1.getId(),ticket,
                TicketStatusConstant.TICKET_STATUS_EFFECTIVE.getCode(),
                new Date(System.currentTimeMillis() + expiredSecond*1000));
//        loginTicketDao.saveLoginTicket(loginTicket);
        //将登录凭证保存在redis中，不用设置过期时间，方便登录的统计，
        String loginTicketKey = RedisKeyUtils.getLoginTicketKey(ticket);
        redisTemplate.opsForValue().set(loginTicketKey, JSONObject.toJSONString(loginTicket));


        //向上传递ticket交由cookie保存方便下次登录
        map.put("ticket",ticket);
        return map;
    }
}
