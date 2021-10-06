package com.nowcoder.community.vo;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:10
 * @Description: 私信列表展示信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePageVO {
    private PageInfo<Message> pageInfo;
    private List<ConversationVO> conversationVOS;
    private long totalOfUnread;//总的未读消息
}