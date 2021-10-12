package com.nowcoder.community.interceptor;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/20:42
 * @Description: 登录检查拦截器
 */
//使用Spring Security代替
//@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Object handler  目标方法
        log.info("登录拦截器:{}",handler.getClass());
        if(handler instanceof HandlerMethod){
           HandlerMethod method = (HandlerMethod) handler;
            LoginCheck loginCheck = method.getMethodAnnotation(LoginCheck.class);
            if(loginCheck != null){
                User user = LoginInterceptor.users.get();
                if(user == null){
                    response.sendRedirect(request.getContextPath()+ "/toLogin");
                    return false;
                }
            }
        }
        return true;
    }
}