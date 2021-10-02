package com.nowcoder.community;

import com.nowcoder.community.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/02/21:23
 * @Description:
 */
@SpringBootTest
public class MailTest {
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    void testSendMail(){
        mailUtil.sendMail("13887096914@163.com","TEST","welcome spring mail");
    }
    //测试发送html
    @Test
    void testSendHtml(){
        Context context = new Context();
        context.setVariable("username","huawei");
        String comment = templateEngine.process("/mail/mail", context);
        mailUtil.sendMail("13887096914@163.com","welcome",comment);
    }
}