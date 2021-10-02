package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/02/23:49
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //针对只是页面跳转的页面直接使用viewController
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toRegister").setViewName("/site/register");
        registry.addViewController("/toLogin").setViewName("/site/login");
    }
}