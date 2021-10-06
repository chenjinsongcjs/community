package com.nowcoder.community.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.MessageStatus;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.MessageDao;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.vo.ConversationPageVo;
import com.nowcoder.community.vo.ConversationVO;
import com.nowcoder.community.vo.MessagePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:21
 * @Description:
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserDao userDao;
    @Override
    public MessagePageVO getAllMessageByPage(int userId, int pageNum) {
        Page<Message> messages = PageHelper.startPage(pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        //当前用户下的所有会话
        List<Message> messagesThisUser = messageDao.getAllMessagesThisUser(userId);
        //构建会话对象
        List<ConversationVO> conversationVOS = messagesThisUser.stream().map(message -> {
            ConversationVO conversationVO = new ConversationVO();
            conversationVO.setMessage(message);
            //发消息的对象
            int selectUserId = message.getFromId() == userId ? message.getToId() : message.getFromId();
            User user = userDao.getUserById(selectUserId);
            conversationVO.setUser(user);//会话的另一个对象
            long countOfAllMessage = messageDao.getCountOfAllMessage(message.getConversationId());
            conversationVO.setCountOfMessage(countOfAllMessage);
            long countOfUnread = messageDao.getCountOfUnread(message.getConversationId());
            conversationVO.setCountOfUnread(countOfUnread);
            return conversationVO;
        }).collect(Collectors.toList());
        long totalUnreadMsg = 0;
        for (ConversationVO conversationVO : conversationVOS) {
            totalUnreadMsg += conversationVO.getCountOfUnread();
        }
        PageInfo<Message> pageInfo = new PageInfo<>(messages,PageConstant.NAVIGATE_PAGES);
        return new MessagePageVO(pageInfo,conversationVOS,totalUnreadMsg);
    }

    @Override
    public ConversationPageVo getAllConversationMsgByPage(String conversationId, int pageNum) {
        if(conversationId == null)
            throw new IllegalArgumentException("会话id不能为空");
        Page<Message> msg = PageHelper.startPage(pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        List<Message> allMessageThisConversation = messageDao.getAllMessageThisConversation(conversationId);
        List<ConversationVO> messages = allMessageThisConversation.stream().map(message -> {
            ConversationVO conversationVO = new ConversationVO();
            conversationVO.setMessage(message);
            User user = userDao.getUserById(message.getFromId());
            //设置消息发送者
            conversationVO.setUser(user);
            return conversationVO;
        }).collect(Collectors.toList());
        User user = LoginInterceptor.users.get();
        for (ConversationVO message : messages) {
            if (message.getUser().getId() != user.getId()){
                user = userDao.getUserById(message.getUser().getId());
                break;
            }
        }
        PageInfo<Message> pageInfo = new PageInfo<>(msg, PageConstant.NAVIGATE_PAGES);
        return new ConversationPageVo(pageInfo,messages,user);
    }

    @Override
    public int deleteMessage(int id) {
        return messageDao.updateMessageStatus(id, MessageStatus.MESSAGE_STATUS_DELETE.getCode());
    }
}