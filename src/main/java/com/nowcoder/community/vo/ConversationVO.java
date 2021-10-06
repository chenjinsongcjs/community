package com.nowcoder.community.vo;

import com.nowcoder.community.domain.Message;
import com.nowcoder.community.domain.User;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:12
 * @Description:
 */
@Data
public class ConversationVO {
    private Message message;
    private User user;//发送者 可以是自己或者他人
    private long countOfUnread;//每个会话的未读消息
    private long countOfMessage;//每个会话的所有消息数量

}