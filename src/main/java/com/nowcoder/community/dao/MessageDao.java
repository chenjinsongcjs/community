package com.nowcoder.community.dao;

import com.nowcoder.community.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/2:30
 * @Description: 私信列表服务
 */
@Mapper
public interface MessageDao {
    //分页查询当前用户的所有私信，只显示最后一条没有读的消息
    List<Message> getAllMessagesThisUser(@Param("userId") int userId);
    //查询每一个会话的未读消息数量
    long getCountOfUnread(@Param("conversationId") String conversationId);
    //每个会话的总消息条数
    long getCountOfAllMessage(@Param("conversationId")String conversationId);
    //当前用户的所有未读消息数量
    long getTotalUnreadMessage(@Param("userId") int userId);

    //分页查询当前会话的所有消息
    List<Message> getAllMessageThisConversation(@Param("conversationId") String conversationId);
    //设置消息的状态
    int updateMessageStatus(@Param("ids") List<Integer> ids,@Param("status") int status);
    //保存私信
    int saveMessage(Message message);
    //处理系统通知的SQL
    //查询最新的评论、点赞、关注信息
    Message getLatestNotice(@Param("userId") int userId,@Param("topic") String topic);
    //查询每个主题的未读数量
    long getUnreadNoticeCount(@Param("userId") int userId,@Param("topic") String topic);
    //查询每个主题下的所有消息数量
    long getAllMessageNoticeCount(@Param("userId") int userId,@Param("topic") String topic);
    //获取系统所有未读消息数量
    long getAllUnreadNoticeCount(@Param("userId") int userId);
    //获取每个主题所有的消息
    List<Message> getAllNotice(@Param("userId") int userId,@Param("topic") String topic);

}
