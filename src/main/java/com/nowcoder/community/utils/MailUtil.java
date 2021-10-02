package com.nowcoder.community.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/02/20:51
 * @Description: 邮件发送工具类
 */
@Slf4j
@Component
public class MailUtil {
    @Autowired
    private JavaMailSender javaMailSender ;
    @Value("${spring.mail.username}")
    private String from;//发件人

    //加入thymeleaf模板引擎  ,用模板引擎包装内容，再将内容发送出去
//    private TemplateEngine templateEngine;
    /**
    * @Description: 发送邮件的方法
    * @Param: [to, title, comment]
    * @return: [java.lang.String, java.lang.String, java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/2
    */
    public void sendMail(String to,String title,String comment){
        try {
            //创建mime媒体类型
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //帮助创建mime媒体类型
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            //发件人
            helper.setFrom(from);
            //收件人
            helper.setTo(to);
            //标题
            helper.setSubject(title);
            //发送的主体内容,第二个参数不能忘记不然不能发送html
            helper.setText(comment,true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
         log.error("邮件发送失败"+e.getMessage());
        }
    }

}