package com.nowcoder.community.controller;

import com.github.pagehelper.Page;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.EventConstant;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.dto.FollowerDto;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.kafka.KafKaProducer;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.vo.FollowPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/22:51
 * @Description: 粉丝控制器
 */
@Slf4j
@Controller
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private KafKaProducer producer;
    @GetMapping("/followeeList/{userId}")
    public String getFolloweeList(@RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum,
                                  Model model,
                                  @PathVariable("userId") int userId){
        FollowPageVo followeeOfMine = followService.getFolloweeOfMine(CommentConstant.ENTITY_TYPE_USER.getCode(),userId, pageNum, PageConstant.FOLLOW_PAGE_SIZE);
        log.info("关注的对象返回页面：{}",followeeOfMine);
        model.addAttribute("page",followeeOfMine);
        model.addAttribute("requestPath","/follow/followeeList/"+userId);
        User user = userService.getUserById(userId);
        model.addAttribute("user",user);
        return "/site/followee";
    }
    @GetMapping("/followerList/{userId}")
    public String getFollowerList(@RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum,
                                  Model model,
                                  @PathVariable("userId") int userId){
        FollowPageVo followerOfMine = followService.getFollowerOfMine(CommentConstant.ENTITY_TYPE_USER.getCode(),userId, pageNum, PageConstant.FOLLOW_PAGE_SIZE);
        log.info("粉丝返回页面：{}",followerOfMine);
        model.addAttribute("page",followerOfMine);
        //分页使用的请求路径
        model.addAttribute("requestPath","/follow/followerList/"+userId);
        User user = userService.getUserById(userId);
        model.addAttribute("user",user);
        return "/site/follower";
    }
    //关注或者取消关注某个人
    @PostMapping("/follow")
    @ResponseBody
    public String follow(int userId){
        followService.follow(CommentConstant.ENTITY_TYPE_USER.getCode(),userId);
        //触发关注事件，发送系统消息
        Event event = new Event().setTopic(EventConstant.event_follow)
                .setEntityType(CommentConstant.ENTITY_TYPE_USER.getCode())
                .setUserId(LoginInterceptor.users.get().getId())//触发事件的对象
                .setEntityUserId(userId)
                .setEntityId(userId);
        producer.fireEvent(event);


        return JSONUtils.getJSONString(0);
    }
    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int userId){
        followService.unfollow(CommentConstant.ENTITY_TYPE_USER.getCode(),userId);
        return JSONUtils.getJSONString(0);
    }
}