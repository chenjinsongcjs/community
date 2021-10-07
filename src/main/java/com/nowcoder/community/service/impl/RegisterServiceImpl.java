package com.nowcoder.community.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.nowcoder.community.constant.ActivationConstant;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.RegisterService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.MailUtil;
import com.nowcoder.community.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/1:20
 * @Description:
 */
@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Transactional
    @Override
    public Map<String, String> register(User user) {
        Map<String,String> map = new HashMap<>();
        //校验数据
        if(StringUtils.isEmpty(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(user.getPassword())){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        if(StringUtils.isEmpty(user.getEmail())){
            map.put("EmailMsg","邮箱不能为空");//TODO 邮箱应该做校验，稍后做
            return map;
        }
        //数据库查询数据，进行校验
        User userInDB = userService.getUserByName(user.getUsername());
        //用户名和邮箱不能重复
        if(userInDB != null){
            map.put("usernameMsg","用户名已注册");
            return map;
        }
        User email = userService.getUserByEmail(user.getEmail());
        if(email != null){
            map.put("EmailMsg","该邮箱已注册");
            return map;
        }
        //校验完毕进行真正的注册,设置user信息
        String activationCode = IdUtil.simpleUUID();
        user.setActivationCode(activationCode);
        user.setStatus(0);
        user.setType(0);
        user.setCreateTime(new Date());
        user.setHeaderUrl("https://images.nowcoder.com/head/"+ RandomUtil.randomInt(0,1000) +"t.png");
        String salt = IdUtil.simpleUUID().substring(0, 5);
        user.setSalt(salt);
        String passwd = DigestUtil.md5Hex(user.getPassword() + salt);
        user.setPassword(passwd);
        userService.saveUser(user);
        //发邮件要求激活账号
        //拼接激活路径，在配置文件中定义方便修改
        //TODO 后期发送邮件可以放在队列中慢慢发
        Context context = new Context();
        context.setVariable("username",user.getUsername());
        String url = domain+"/community/"+user.getId()+"/"+activationCode;
        log.info("邮件路径：{}",url);
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation", context);
        mailUtil.sendMail(user.getEmail(),"牛客网激活邮件",content);
        return null;
    }

    @Override
    public int activation(int userId, String activationCode) {
        User user = userService.getUserById(userId);
        String code = user.getActivationCode();
        if (!activationCode.equals(code)){
            return ActivationConstant.ACTIVATION_FAILURE;
        }else{
            if(user.getStatus() == ActivationConstant.ACTIVATION_STATUS_0){
                user.setStatus(ActivationConstant.ACTIVATION_STATUS_1);

                userService.updateUser(user);
                //更新之后清空缓存
                clearCache(userId);
                return ActivationConstant.ACTIVATION_SUCCESS;
            }else{
                return ActivationConstant.ACTIVATION_REPEAT;
            }
        }
    }
    //清除缓存中的数据
    private   void clearCache(int userId){
        String userKey = RedisKeyUtils.getUserKey(userId);
        redisTemplate.delete(userKey);
    }
}