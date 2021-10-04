package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.vo.CommentPage;
import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/23:56
 * @Description:
 */
@Controller
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private CommentService commentService;

    @GetMapping(value = {"/","/index"})
    public String showDiscussPosts(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,                                   Model model){
        MyPage myPage = discussPostService.getDiscussPostByPage(pageNum, PageConstant.PAGE_SIZE);
        model.addAttribute("page", myPage);
        model.addAttribute("requestPath","/index");
        return "index";
    }
    @PostMapping("/discussPost/add")
    @ResponseBody
    @LoginCheck//登录检查，避免走后门
    public String sendDiscussPost(String title,String content){
        User user = LoginInterceptor.users.get();
        if(user == null){
            return JSONUtils.getJSONString(1,"您还没有登录，不能发送帖子");
        }
        return discussPostService.sendDDiscussPost(user.getId(), title, content);
    }
    //查询帖子的详细信息
    @GetMapping("/discussPost/detail/{id}")
    public String getDiscussDetail(@PathVariable("id") int id,Model model,
                                   @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        //帖子信息包括帖子内容和用户信息
        DiscussPostAndUser discussPostAndUser = discussPostService.getDiscussPostDetailById(id);
        model.addAttribute("detail",discussPostAndUser);
        //TODO 帖子的评论信息
        CommentPage page = commentService.getAllCommentByPage(id, pageNum);
        model.addAttribute("page",page);
        model.addAttribute("requestPath","/discussPost/detail/"+id);
        //TODO 回复被人的回复 还未实现，可能需要改变数据结构
        return "/site/discuss-detail";
    }

}