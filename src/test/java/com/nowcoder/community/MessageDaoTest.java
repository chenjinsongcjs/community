package com.nowcoder.community;

import com.nowcoder.community.dao.MessageDao;
import com.nowcoder.community.domain.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:57
 * @Description:
 */
@SpringBootTest
public class MessageDaoTest {
    @Autowired
    private MessageDao messageDao;
    @Test
    void testGetAllMessagesThisUser(){
        List<Message> messages = messageDao.getAllMessagesThisUser(111);
        for (Message message : messages) {
            System.out.println(message);
        }
        System.out.println(messages.size());
    }
    @Test
    void testGetCountOfUnread(){
        System.out.println(messageDao.getCountOfUnread("111_112"));
    }
    @Test
    void testGetCountOfAllMessage(){
        System.out.println(messageDao.getCountOfAllMessage("111_112"));
    }
    @Test
    void testGetAllMessageThisConversation(){
        List<Message> messages = messageDao.getAllMessageThisConversation("111_112");
        for (Message message : messages) {
            System.out.println(message);
        }
    }
}