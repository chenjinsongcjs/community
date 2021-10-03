package com.nowcoder.community.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/1:19
 * @Description:
 */
@Component
public class CookieUtils {

    public String getCookieValue(HttpServletRequest request,String name){
        if(request == null || name == null)//参数检查
            throw new IllegalArgumentException("参数不能为空");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name))
                    return cookie.getValue();
            }
        }
        return null;
    }
}