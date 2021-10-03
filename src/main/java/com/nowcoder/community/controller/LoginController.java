package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
}