package com.nowcoder.community.controller;

import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.nowcoder.community.constant.LoginTicketExpiredTime;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.service.LogoutService;
import com.nowcoder.community.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/21:02
 * @Description: 登录相关的控制器
 */
@Slf4j
@Controller
public class LoginController {
    @Autowired
    private Producer producer;
    @Autowired
    private LoginService loginService;
    @Autowired
    private LogoutService logoutService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    String uuid;
    /**
    * @Description: 获取验证码
    * @Param: []
    * @return: []
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    @GetMapping("/kaptcha.jpg")
    public void getKaptchaCode(HttpServletResponse response){
        String text = producer.createText();
        log.info("验证码：{}",text);
//        session.setAttribute("kaptchaCode",text);
        //将验证码存储在Redis中
        String kaptchaKey = RedisKeyUtils.getKaptchaKey(uuid);
        //60s后过期
        redisTemplate.opsForValue().set(kaptchaKey,text,60, TimeUnit.SECONDS);

        BufferedImage image = producer.createImage(text);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            ImageIO.write(image,"jpg",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/toLogin",method = {RequestMethod.GET})
    public String toLogin(HttpServletResponse response, HttpServletRequest request){
        uuid = IdUtil.simpleUUID();
        Cookie cookie = new Cookie("uuid", uuid);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        return "site/login";
    }

    //登录与登出交由Spring Security管理

    @PostMapping("/login")
    public String login(User user, String kaptchaCode,
                        HttpServletResponse response,
                        Model model,
                        boolean rememberMe,@CookieValue("uuid") String uuid,
                        HttpServletRequest request){
        log.info("用户：{}",user);
        log.info("前端验证码：{}",kaptchaCode);
        //先校验验证码
//        String code = (String) session.getAttribute("kaptchaCode");
        String kaptchaKey = RedisKeyUtils.getKaptchaKey(uuid);
        String code = redisTemplate.opsForValue().get(kaptchaKey);
        if (StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(kaptchaCode) ||
                !code.equalsIgnoreCase(kaptchaCode)){
            model.addAttribute("codeMsg","验证码不正确");
            model.addAttribute("user",user);
            log.info("验证码不正确");
            return "/site/login";
        }
        int expired = rememberMe ? LoginTicketExpiredTime.has_remember_me.getSecond()
                : LoginTicketExpiredTime.no_remember_me.getSecond();
        Map<String, String> map = loginService.login(user, expired);
        log.info("登录情况：{}",map);
        if(map.containsKey("ticket")){
            //登录成功
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setMaxAge(expired);
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
            return "redirect:/index";//重定向到首页
        }
        //登录失败
        model.addAttribute("usernameMsg",map.get("usernameMsg"));
        model.addAttribute("passwordMsg",map.get("passwordMsg"));
        model.addAttribute("user",user);
        return "/site/login";
    }
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){
        logoutService.logout(ticket);

        //退出要清空权限
        SecurityContextHolder.clearContext();
        return "redirect:/toLogin";
    }
}