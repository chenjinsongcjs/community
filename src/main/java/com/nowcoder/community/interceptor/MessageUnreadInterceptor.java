package com.nowcoder.community.interceptor;

import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/09/4:58
 * @Description:
 */
@Component
public class MessageUnreadInterceptor implements HandlerInterceptor {
    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = LoginInterceptor.users.get();
        if(user != null && modelAndView != null){
            long letterCount = messageService.getLetterCount(user.getId());
            long noticeCount = messageService.getNoticeCount(user.getId());
            modelAndView.addObject("totalUnread",letterCount+noticeCount);
        }
    }
}