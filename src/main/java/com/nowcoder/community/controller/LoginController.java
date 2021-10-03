package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.constant.LoginTicketExpiredTime;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.service.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

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
    /**
    * @Description: 获取验证码
    * @Param: []
    * @return: []
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    @GetMapping("/kaptcha.jpg")
    public void getKaptchaCode(HttpServletResponse response, HttpSession session){
        String text = producer.createText();
        log.info("验证码：{}",text);
        session.setAttribute("kaptchaCode",text);
        BufferedImage image = producer.createImage(text);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            ImageIO.write(image,"jpg",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/login")
    public String login(User user, String kaptchaCode,
                        HttpServletResponse response,
                        HttpSession session,
                        Model model,
                        boolean rememberMe){
        log.info("用户：{}",user);
        log.info("前端验证码：{}",kaptchaCode);
        //先校验验证码
        String code = (String) session.getAttribute("kaptchaCode");
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
            cookie.setPath("/community");
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
        return "redirect:/toLogin";
    }
}