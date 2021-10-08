package com.nowcoder.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.EventConstant;
import com.nowcoder.community.constant.MessageStatus;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.MessageDao;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.SensitiveWordsFilter;
import com.nowcoder.community.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:21
 * @Description:
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveWordsFilter filter;

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
            User user = userService.getUserById(selectUserId);
            conversationVO.setUser(user);//会话的另一个对象
            long countOfAllMessage = messageDao.getCountOfAllMessage(message.getConversationId());
            conversationVO.setCountOfMessage(countOfAllMessage);
            long countOfUnread = messageDao.getCountOfUnread(message.getConversationId());
            conversationVO.setCountOfUnread(countOfUnread);
            return conversationVO;
        }).collect(Collectors.toList());
        long totalUnreadMsg = messageDao.getTotalUnreadMessage(userId);
        PageInfo<Message> pageInfo = new PageInfo<>(messages, PageConstant.NAVIGATE_PAGES);
        return new MessagePageVO(pageInfo, conversationVOS, totalUnreadMsg);
    }

    @Override
    public ConversationPageVo getAllConversationMsgByPage(String conversationId, int pageNum) {
        if (conversationId == null)
            throw new IllegalArgumentException("会话id不能为空");
        Page<Message> msg = PageHelper.startPage(pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        List<Message> allMessageThisConversation = messageDao.getAllMessageThisConversation(conversationId);
        List<ConversationVO> messages = allMessageThisConversation.stream().map(message -> {
            ConversationVO conversationVO = new ConversationVO();
            //设置已读
            messageDao.updateMessageStatus(Arrays.asList(message.getId()),MessageStatus.MESSAGE_STATUS_READ.getCode());
            conversationVO.setMessage(message);
            User user = userService.getUserById(message.getFromId());
            //设置消息发送者
            conversationVO.setUser(user);
            return conversationVO;
        }).collect(Collectors.toList());
        User user = LoginInterceptor.users.get();
        for (ConversationVO message : messages) {
            if (message.getUser().getId() != user.getId()) {
                user = userService.getUserById(message.getUser().getId());
                break;
            }
        }
        PageInfo<Message> pageInfo = new PageInfo<>(msg, PageConstant.NAVIGATE_PAGES);
        //当前页的消息设置为已读

        return new ConversationPageVo(pageInfo, messages, user);
    }

    @Override
    public int deleteMessage(int id) {
        return messageDao.updateMessageStatus(Arrays.asList(id), MessageStatus.MESSAGE_STATUS_DELETE.getCode());
    }

    @Override
    public int sendMessage(String toName, String content) {
        Message message = new Message();
        //信息完整封装
        User user = LoginInterceptor.users.get();
        if (user == null)
            throw new RuntimeException("用户未登录");
        message.setFromId(user.getId());
        //对私信内容进行敏感词处理
        content = HtmlUtils.htmlEscape(content);
        content = filter.filter(content);
        message.setContent(content);
        User toUser = userService.getUserByName(toName);
        if(toUser == null)
            throw new RuntimeException("接收者账号不存在");
        message.setToId(toUser.getId());
        String conversationId = "";
        if(user.getId() < toUser.getId()){
            conversationId = user.getId() +"_"+ toUser.getId();
        }else {
            conversationId = toUser.getId() +"_"+user.getId();
        }
        message.setConversationId(conversationId);
        message.setCreateTime(new Date());
        return messageDao.saveMessage(message);
    }

    @Override
    public int saveMessage(Message message) {
        return messageDao.saveMessage(message);
    }

    @Override
    public SystemNoticeVo getSystemNoticeList(int userId) {
        NoticeLikeVo noticeLikeVo = new NoticeLikeVo();
        NoticeCommentVo noticeCommentVo = new NoticeCommentVo();
        NoticeFollowVo noticeFollowVo = new NoticeFollowVo();
        boolean buildNoticeVo = buildNoticeVo(userId, EventConstant.event_like, noticeLikeVo, null, null);
        boolean buildNoticeVo1 = buildNoticeVo(userId, EventConstant.event_follow, null, noticeFollowVo, null);
        boolean buildNoticeVo2 = buildNoticeVo(userId, EventConstant.event_comment, null, null, noticeCommentVo);
        long allUnreadNoticeCount = messageDao.getAllUnreadNoticeCount(userId);
        SystemNoticeVo systemNoticeVo = new SystemNoticeVo();
        if (buildNoticeVo2)
            systemNoticeVo.setNoticeCommentVo(noticeCommentVo);
        if(buildNoticeVo1)
            systemNoticeVo.setNoticeFollowVo(noticeFollowVo);
        if (buildNoticeVo)
            systemNoticeVo.setNoticeLikeVo(noticeLikeVo);
        systemNoticeVo.setAllUnreadNoticeCount(allUnreadNoticeCount);
        log.info("系统通知页面对象：{}",systemNoticeVo);
        return systemNoticeVo;
    }

    @Override
    public long getLetterCount(int userId) {
        return messageDao.getTotalUnreadMessage(userId);
    }

    @Override
    public long getNoticeCount(int userId) {
        return messageDao.getAllUnreadNoticeCount(userId);
    }

    @Override
    public NoticeLikePage getNoticeLikePage(int userId,int pageNum) {
        Page<Message> likes = PageHelper.startPage(pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        User fromUser = userService.getUserById(1);//系统用户
        List<Message> result = messageDao.getAllNotice(userId, EventConstant.event_like);
        if(result != null && result.size() > 0){
            List<NoticeLikeVo> collect = result.stream().map(message -> {
                //设置已读
                messageDao.updateMessageStatus(Arrays.asList(message.getId()),MessageStatus.MESSAGE_STATUS_READ.getCode());
                NoticeLikeVo noticeLikeVo = new NoticeLikeVo();
                noticeLikeVo.setFromUser(fromUser);
                String content = message.getContent();
                HashMap map = JSONObject.parseObject(content, HashMap.class);
                Object userId1 = map.get("userId");
                User likeUser = null;
                if(userId1 != null)
                    likeUser = userService.getUserById((Integer) userId1);
                noticeLikeVo.setLikeUser(likeUser);
                Object entityType = map.get("entityType");
                if(entityType != null)
                    noticeLikeVo.setEntityType((Integer) entityType);
                Object postId = map.get("postId");
                if(postId != null)
                    noticeLikeVo.setPostId((Integer) postId);
                return noticeLikeVo;
            }).collect(Collectors.toList());
            PageInfo<Message> pageInfo = new PageInfo<>(likes, PageConstant.NAVIGATE_PAGES);
            return new NoticeLikePage(pageInfo,collect);
        }
       return null;
    }

    @Override
    public NoticeFollowPage getNoticeFollowPage(int userId,int pageNum) {
        Page<Message> follows = PageHelper.startPage(pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        User fromUser = userService.getUserById(1);//系统用户
        List<Message> result = messageDao.getAllNotice(userId, EventConstant.event_follow);
        if(result != null && result.size() >0){
            List<NoticeFollowVo> collect = result.stream().map(message -> {
                messageDao.updateMessageStatus(Arrays.asList(message.getId()),MessageStatus.MESSAGE_STATUS_READ.getCode());
                NoticeFollowVo noticeFollowVo = new NoticeFollowVo();
                noticeFollowVo.setFromUser(fromUser);
                String content = message.getContent();
                HashMap map = JSONObject.parseObject(content, HashMap.class);
                Object userId1 = map.get("userId");
                User likeUser = null;
                if(userId1 != null)
                    likeUser = userService.getUserById((Integer) userId1);
                noticeFollowVo.setLikeUser(likeUser);
                return noticeFollowVo;
            }).collect(Collectors.toList());
            PageInfo<Message> pageInfo = new PageInfo<>(follows, PageConstant.NAVIGATE_PAGES);
            return new NoticeFollowPage(pageInfo,collect);
        }
        return null;
    }

    @Override
    public NoticeCommentPage getNoticeCommentPage(int userId,int pageNum) {
        Page<Message> comments = PageHelper.startPage(pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        User fromUser = userService.getUserById(1);//系统用户
        List<Message> result = messageDao.getAllNotice(userId, EventConstant.event_comment);
        if(result != null &&result.size() >0){
            List<NoticeCommentVo> collect = result.stream().map(message -> {
                messageDao.updateMessageStatus(Arrays.asList(message.getId()),MessageStatus.MESSAGE_STATUS_READ.getCode());
                NoticeCommentVo noticeCommentVo = new NoticeCommentVo();
                noticeCommentVo.setFromUser(fromUser);
                String content = message.getContent();
                HashMap map = JSONObject.parseObject(content, HashMap.class);
                Object userId1 = map.get("userId");
                User likeUser = null;
                if(userId1 != null)
                    likeUser = userService.getUserById((Integer) userId1);
                noticeCommentVo.setLikeUser(likeUser);
                Object entityType = map.get("entityType");
                if(entityType != null)
                    noticeCommentVo.setEntityType((Integer) entityType);
                Object postId = map.get("postId");
                if(postId != null)
                    noticeCommentVo.setPostId((Integer) postId);
                return noticeCommentVo;
            }).collect(Collectors.toList());

            PageInfo<Message> pageInfo = new PageInfo<>(comments, PageConstant.NAVIGATE_PAGES);
            return new NoticeCommentPage(pageInfo,collect);
        }
        return null;
    }


    private boolean buildNoticeVo(int userId,String topic,
                                  NoticeLikeVo noticeLikeVo,
                                  NoticeFollowVo noticeFollowVo,
                                  NoticeCommentVo noticeCommentVo) {
        User fromUser = userService.getUserById(1);//获取系统用户,可能被查询多次
        Message latestNotice = messageDao.getLatestNotice(userId, topic);
        if(latestNotice == null)
            return false;
        long unreadNoticeCount = messageDao.getUnreadNoticeCount(userId, topic);
        long allMessageNoticeCount = messageDao.getAllMessageNoticeCount(userId, topic);
        String content = latestNotice.getContent();
        HashMap map = JSONObject.parseObject(content, HashMap.class);
        Integer userId1 = (Integer) map.get("userId");
        User likeUser = null;
        if (userId1 != null){
            likeUser = userService.getUserById(userId1);
        }
        Object entityType = map.get("entityType");
        if (noticeLikeVo != null){
            noticeLikeVo.setLikeUser(likeUser);
            noticeLikeVo.setAllNoticeCount(allMessageNoticeCount);
            noticeLikeVo.setFromUser(fromUser);
            noticeLikeVo.setUnReadCount(unreadNoticeCount);
            noticeLikeVo.setCreateTime(latestNotice.getCreateTime());
            if(entityType != null)
                noticeLikeVo.setEntityType((Integer) entityType);
        }else if(noticeFollowVo != null){
            noticeFollowVo.setLikeUser(likeUser);
            noticeFollowVo.setAllNoticeCount(allMessageNoticeCount);
            noticeFollowVo.setFromUser(fromUser);
            noticeFollowVo.setUnReadCount(unreadNoticeCount);
            noticeFollowVo.setCreateTime(latestNotice.getCreateTime());
        }else {
            noticeCommentVo.setLikeUser(likeUser);
            noticeCommentVo.setAllNoticeCount(allMessageNoticeCount);
            noticeCommentVo.setFromUser(fromUser);
            noticeCommentVo.setUnReadCount(unreadNoticeCount);
            noticeCommentVo.setCreateTime(latestNotice.getCreateTime());
            if(entityType != null)
                noticeCommentVo.setEntityType((Integer) entityType);
        }
        return true;
    }
}