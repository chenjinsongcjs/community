package com.nowcoder.community.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nowcoder.community.constant.TicketStatusConstant;
import com.nowcoder.community.dao.LoginTicketDao;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.LoginTicket;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.utils.CookieUtils;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/1:16
 * @Description: 拦截器拦截所有的请求，显示用户信息，不同的请求有不同的显示，批量处理
 * 拦截器要配置进WebMVC中才能起作用
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private CookieUtils cookieUtils;
//    @Autowired
//    private LoginTicketDao loginTicketDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;

    //用ThreadLocal存储用户信息，使得线程之间隔离,在任何地方任何时候都能获取
    public static ThreadLocal<User> users = new ThreadLocal<>();

    //在目标方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户的登录信息（从Cookie中）
        String ticket = cookieUtils.getCookieValue(request, "ticket");
        //根据ticket获取用户信息，不过之前要判断ticket的有效性
//        LoginTicket loginTicket = loginTicketDao.getLoginTicketByTicket(ticket);
        String ticketKey = RedisKeyUtils.getLoginTicketKey(ticket);
        String lt = redisTemplate.opsForValue().get(ticketKey);
        LoginTicket loginTicket = JSONObject.parseObject(lt, new TypeReference<LoginTicket>() {
        });
        if(loginTicket != null &&
                loginTicket.getStatus() != TicketStatusConstant.TICKET_STATUS_INVALID.getCode() &&
        loginTicket.getExpired().after(new Date())){
            User user = userDao.getUserById(loginTicket.getUserId());
            users.set(user);
        }
        //true放行，false直接返回不执行目标方法，执行链结束
        return true;
    }
    //在目标方法之后执行 模板渲染之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //将用户信息放在模板中，方便读取
        if(modelAndView != null)
            modelAndView.addObject("loginUser",users.get());
    }
    //在模板结束之后执行或者发生异常执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //模板结束之后清空ThreadLocal，避免内存溢出特别是线程池中，因为key为弱引用
        users.remove();
    }
}