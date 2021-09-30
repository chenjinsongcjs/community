package com.nowcoder.community.controller;

import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = {"/","/index"})
    public String showDiscussPosts(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,                                   Model model){
        MyPage myPage = discussPostService.getDiscussPostByPage(pageNum, PageConstant.PAGE_SIZE);
        model.addAttribute("page", myPage);
//        System.out.println(myPage.getPageInfo());
        return "index";
    }

}