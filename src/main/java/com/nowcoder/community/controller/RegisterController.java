package com.nowcoder.community.controller;

import com.nowcoder.community.constant.ActivationConstant;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/0:05
 * @Description:
 */
@Controller
@Slf4j
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    /**
    * @Description: 处理注册的controller，对前端传送的数据进行校验
    * @Param: [model, user]
    * @return: [org.springframework.ui.Model, com.nowcoder.community.domain.User]
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    @PostMapping("/register")
    public String register(@Valid User user,Model model){
        Map<String, String> msg = registerService.register(user);
        if(msg == null){//注册成功，先去第三方页面，再去登录页面
            model.addAttribute("msg","您的账号已经注册成功,请尽快激活!");
            return "/site/operate-result";
        }
        //注册失败，返回注册页
        model.addAttribute("usernameMsg",msg.get("usernameMsg"));
        model.addAttribute("passwordMsg",msg.get("passwordMsg"));
        model.addAttribute("emailMsg",msg.get("EmailMsg"));
        model.addAttribute("user",user);//方便thymeleaf提示，其实user会自动进入model中
        log.info("错误信息：{}",msg);
        return "/site/register";
    }
    @GetMapping("/community/{userId}/{activationCode}")
    public String activation(@PathVariable("userId") int userId,
                             @PathVariable("activationCode") String activationCode,
                             Model model){
       int ac = registerService.activation(userId,activationCode);
       if(ac == ActivationConstant.ACTIVATION_FAILURE){
           model.addAttribute("msg","激活码错误，请重新激活");
           model.addAttribute("url","/index");//激活失败跳转首页
       }else if(ac == ActivationConstant.ACTIVATION_REPEAT){
           model.addAttribute("msg","您的账号已经激活，请勿重复操作");
           model.addAttribute("url","/index");//重复激活跳转首页
       }else{
           model.addAttribute("msg","您的账号已经激活成功,可以正常使用了!");
           model.addAttribute("url","/toLogin");//激活失败跳转首页
       }
        return "/site/operate-result";
    }
}