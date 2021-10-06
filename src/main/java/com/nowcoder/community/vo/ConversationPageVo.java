package com.nowcoder.community.vo;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/3:41
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationPageVo {
    private PageInfo<Message> pageInfo;
    private List<ConversationVO> messages;
    private User fromUser;
}