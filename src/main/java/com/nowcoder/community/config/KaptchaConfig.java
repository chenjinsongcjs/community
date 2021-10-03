package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/20:41
 * @Description: Google验证码相关配置
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public Producer producer(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //基本配置,查看com.google.code.kaptcha.util.Config类进行配置参数查看
        properties.setProperty("kaptcha.image.width","100");//验证码的宽度
        properties.setProperty("kaptcha.image.height","40");//验证码的高度
        //取消验证码的噪音干扰
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        /**
         * kaptcha.textproducer.font.color //文本颜色配置
         * kaptcha.textproducer.font.size //字体大小
         * kaptcha.textproducer.char.length //验证码长度
         * kaptcha.textproducer.char.string ///验证码截取的根文本
         */
        properties.setProperty("kaptcha.textproducer.font.size","30");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}