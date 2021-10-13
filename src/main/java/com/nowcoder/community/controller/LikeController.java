package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.EventConstant;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.kafka.KafKaProducer;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/6:43
 * @Description:  处理点赞的控制器，主要是AJAX请求返回JSON数据
 */
@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private KafKaProducer producer;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/like")
    @LoginCheck
    public String like(int entityType, int entityId, int entityUserId,int postId){
        //肯定只有当前登录用户才能点赞
        User user = LoginInterceptor.users.get();//进行了登录拦截，没有登录到不了这里
        likeService.like(entityType,entityId,user.getId(),entityUserId);
        boolean likeStatus = likeService.likeStatus(entityType, entityId);
        long likeCount = likeService.getLikeCount(entityType, entityId);
        Map<String,Object> map = new HashMap<>();
        map.put("likeStatus",likeStatus);
        map.put("likeCount",likeCount);
        //只有点赞才发送
        if(likeStatus){
            //触发点赞事件 ，发送系统通知
            Event event = new Event().setTopic(EventConstant.event_like)
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setUserId(user.getId())
                    .setData("postId",postId);
            producer.fireEvent(event);
        }
        //将评论帖子放入Redis中，方便热帖排行
        String postRefreshKey = RedisKeyUtils.getPostRefreshKey();
        redisTemplate.opsForSet().add(postRefreshKey,postId+"");

        return JSONUtils.getJSONString(0,null,map);
    }
}