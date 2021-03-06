package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.constant.DiscussPostStatus;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticSearchService;
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


    @RequestMapping(value = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String showDiscussPosts(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                   Model model,@RequestParam(value = "orderModel",defaultValue = "0") int orderModel){
        MyPage myPage = discussPostService.getDiscussPostByPage(pageNum, PageConstant.PAGE_SIZE,orderModel);
        model.addAttribute("page", myPage);
        model.addAttribute("requestPath","/index");
        model.addAttribute("orderModel",orderModel);
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
        return "/site/discuss-detail";
    }
    @PostMapping("/top")
    @ResponseBody
    public String topPost(int postId){
        int topPost = discussPostService.topPost(postId);
        if (topPost == 0)
            return JSONUtils.getJSONString(1,"帖子置顶失败");
        return JSONUtils.getJSONString(0,"帖子置顶成功");
    }
    @PostMapping("/update/{status}")
    @ResponseBody
    public String update(@PathVariable("status") int status, int postId){
        if (status == DiscussPostStatus.POST_STATUS_WONDERFUL.getCode()){
            int wonderfulPost = discussPostService.wonderfulPost(postId);
            if (wonderfulPost == 0)
                return JSONUtils.getJSONString(1,"帖子精华失败");
            return JSONUtils.getJSONString(0,"帖子精华成功");
        }else if (status == DiscussPostStatus.POST_STATUS_DELETE.getCode()){
            int deletePost = discussPostService.deletePost(postId);
            if (deletePost == 0)
                return JSONUtils.getJSONString(1,"帖子删除失败");
            return JSONUtils.getJSONString(0,"帖子删除成功");
        }else{
            return JSONUtils.getJSONString(1,"更新状态失败");
        }


    }

}