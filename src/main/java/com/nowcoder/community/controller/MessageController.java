package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.vo.ConversationPageVo;
import com.nowcoder.community.vo.MessagePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        session.setAttribute("countOfAllUnreadMessages",page.getTotalOfUnread());
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
}