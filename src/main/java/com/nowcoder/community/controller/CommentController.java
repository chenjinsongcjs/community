package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/22:53
 * @Description: 处理评论回复
 */
@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/reply")
    @LoginCheck
    public String saveComment(Comment comment, HttpServletRequest request){
        //构建完整的评论实体存入数据库
        User user = LoginInterceptor.users.get();
        log.info("前端评论对象：{}",comment);
        comment.setUserId(user.getId());
        comment.setCreateTime(new Date());
        comment.setStatus(0);
        commentService.saveComment(comment);
        return "redirect:"+ request.getHeader("Referer");
    }
}