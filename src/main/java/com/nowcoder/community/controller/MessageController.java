package com.nowcoder.community.controller;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.constant.EventConstant;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/4:21
 * @Description: 私信控制器
 *
 */
@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    @LoginCheck
    public String getMessageList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                 Model model,
                                 HttpSession session){
        User user = LoginInterceptor.users.get();
        if(user == null)
            throw new RuntimeException("用户未登录");
        MessagePageVO page = messageService.getAllMessageByPage(user.getId(), pageNum);
        model.addAttribute("page",page);
        model.addAttribute("requestPath","/message/list");

        long noticeCount = messageService.getNoticeCount(user.getId());
        model.addAttribute("noticeUnreadCount",noticeCount);
        return "/site/letter";

    }
    @GetMapping("/conversation/{conversionId}")
    @LoginCheck
    public String getConversationList(@PathVariable("conversionId") String conversionId,
                                      @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      Model model){
        ConversationPageVo page = messageService.getAllConversationMsgByPage(conversionId, pageNum);
        model.addAttribute("page",page);
        model.addAttribute("requestPath","/message/conversation/"+conversionId);
        return "/site/letter-detail";
    }
    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") int id, RedirectAttributes model, HttpServletRequest request){
        int i = messageService.deleteMessage(id);
        if(i == 0)
            model.addAttribute("deleteMsg","消息删除失败");
        else
            model.addAttribute("deleteMsg","消息删除成功");
        return "redirect:"+request.getHeader("Referer");
    }
    @PostMapping("/send")
    @ResponseBody
    public String sendMessage(String toName,String content){
        //构建信息，前端传来发送对象和内容
        int i = messageService.sendMessage(toName, content);
        if (i == 0)
            return JSONUtils.getJSONString(1,"信息发送失败");
        else
            return JSONUtils.getJSONString(0,"信息发送成功");
    }

    @GetMapping("/notice")
    public String getNoticeList(Model model){
        SystemNoticeVo systemNoticeList = messageService.getSystemNoticeList(LoginInterceptor.users.get().getId());
        model.addAttribute("notice",systemNoticeList);

        model.addAttribute("unReadLetterCount",messageService.getLetterCount(LoginInterceptor.users.get().getId()));
        return "site/notice";
    }
    @GetMapping("/noticeDetail/{topic}")
    @LoginCheck
    public String gotoNoticeDetail(@PathVariable("topic") String topic,
                                   @RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum,
                                   Model model,HttpServletRequest request){
        User user = LoginInterceptor.users.get();
        if (EventConstant.event_like.equals(topic)){
            NoticeLikePage noticeLikePage = messageService.getNoticeLikePage(user.getId(), pageNum);
            model.addAttribute("page",noticeLikePage);
        }else if(EventConstant.event_comment.equals(topic)){
            NoticeCommentPage noticeCommentPage = messageService.getNoticeCommentPage(user.getId(), pageNum);
            model.addAttribute("page",noticeCommentPage);
        }else{
            NoticeFollowPage noticeFollowPage = messageService.getNoticeFollowPage(user.getId(), pageNum);
            model.addAttribute("page",noticeFollowPage);
        }
        model.addAttribute("topic",topic);
        model.addAttribute("requestPath","/message/noticeDetail/"+topic);
        return "site/notice-detail";
    }
}