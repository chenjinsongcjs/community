package com.nowcoder.community.config;

import com.nowcoder.community.interceptor.LoginCheckInterceptor;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.interceptor.MessageUnreadInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Autowired
    private MessageUnreadInterceptor messageUnreadInterceptor;

    //针对只是页面跳转的页面直接使用viewController
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toRegister").setViewName("site/register");
        registry.addViewController("/toLogin").setViewName("site/login");
//        registry.addViewController("/user/setting").setViewName("/site/setting");
        registry.addViewController("/error").setViewName("error/500");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");//静态资源放行

        registry.addInterceptor(loginCheckInterceptor)
                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");//静态资源放行

        registry.addInterceptor(messageUnreadInterceptor)
                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");//静态资源放行


    }
}