package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/like")
    @LoginCheck
    public String like(int entityType, int entityId, int entityUserId){
        //肯定只有当前登录用户才能点赞
        User user = LoginInterceptor.users.get();//进行了登录拦截，没有登录到不了这里
        likeService.like(entityType,entityId,user.getId(),entityUserId);
        boolean likeStatus = likeService.likeStatus(entityType, entityId);
        long likeCount = likeService.getLikeCount(entityType, entityId);
        Map<String,Object> map = new HashMap<>();
        map.put("likeStatus",likeStatus);
        map.put("likeCount",likeCount);
        return JSONUtils.getJSONString(0,null,map);
    }
}