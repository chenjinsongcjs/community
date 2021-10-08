package com.nowcoder.community.service;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.vo.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:08
 * @Description: 私信服务
 */
public interface MessageService {
    //获取私信分页列表
    MessagePageVO getAllMessageByPage(int userId,int pageNum);
    //获取会话的信息列表，分页显示
    ConversationPageVo getAllConversationMsgByPage(String conversationId,int pageNum);
    //删除消息
    int deleteMessage(int id);
    //发送私信功能实现
    int sendMessage(String tiName ,String content);
    //保存消息对象
    int saveMessage(Message message);
    //获取系统列表通知
    SystemNoticeVo getSystemNoticeList(int userId);
    //获取朋友私信数量
    long getLetterCount(int userId);
    //获取系统通知数量
    long getNoticeCount(int userId);
    // 进入系统通知详情页
    NoticeLikePage getNoticeLikePage(int userId,int pageNum);
    NoticeFollowPage getNoticeFollowPage(int userId,int pageNum);
    NoticeCommentPage getNoticeCommentPage(int userId,int pageNum);

}
