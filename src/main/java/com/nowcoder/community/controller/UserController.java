package com.nowcoder.community.controller;

import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/8:18
 * @Description: 个人中心的控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;

    @GetMapping("/profile/{userId}")
    public String toProfile(@PathVariable("userId") int userId, Model model){
        //去往个人中心
        User user = userService.getUserById(userId);
        //用户的获赞情况
        long likeCountOfUser = likeService.getAllLikeCountOfUser(userId);
        model.addAttribute("user",user);
        model.addAttribute("likeCount",likeCountOfUser);
        //关注状态
        boolean followStatus = followService.followStatus(CommentConstant.ENTITY_TYPE_USER.getCode(), userId);
        model.addAttribute("followStatus",followStatus);
        //关注数量
        long followeeCount = followService.followeeCount(CommentConstant.ENTITY_TYPE_USER.getCode(),userId);
        //粉丝数量
        long followerCount = followService.followerCount(CommentConstant.ENTITY_TYPE_USER.getCode(),userId);
        model.addAttribute("followeeCount",followeeCount);
        model.addAttribute("followerCount",followerCount);

        return "/site/profile";
    }
}