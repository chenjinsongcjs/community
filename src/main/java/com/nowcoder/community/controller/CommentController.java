package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.EventConstant;
import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.kafka.KafKaProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private KafKaProducer producer;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/reply")
    @LoginCheck
    public String saveComment(Comment comment,
                              int entityUserId,
                              int discussPost,
                              HttpServletRequest request){
        //构建完整的评论实体存入数据库
        User user = LoginInterceptor.users.get();
        log.info("前端评论对象：{}",comment);
        comment.setUserId(user.getId());
        comment.setCreateTime(new Date());
        comment.setStatus(0);
        commentService.saveComment(comment);
        //触发评论事件
        Event event = new Event().setTopic(EventConstant.event_comment)
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setUserId(user.getId())
                .setEntityUserId(entityUserId)
                .setData("postId",discussPost);
        producer.fireEvent(event);
        //评论时 ，异步地将帖子修改后放入es中
        Event event1 = new Event().setTopic(EventConstant.event_publish)
                .setData("postId", discussPost);
        producer.fireEvent(event1);

        //将评论帖子放入Redis中，方便热帖排行
        String postRefreshKey = RedisKeyUtils.getPostRefreshKey();
        redisTemplate.opsForSet().add(postRefreshKey,discussPost+"");


        return "redirect:"+ request.getHeader("Referer");
    }
}