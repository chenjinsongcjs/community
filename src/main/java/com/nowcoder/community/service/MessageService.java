package com.nowcoder.community.service;

import com.nowcoder.community.domain.Message;
import com.nowcoder.community.vo.ConversationPageVo;
import com.nowcoder.community.vo.MessagePageVO;

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

}
