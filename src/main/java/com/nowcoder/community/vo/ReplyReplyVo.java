package com.nowcoder.community.vo;

import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.User;
import lombok.Data;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:44
 * @Description: 给回复的评论
 */
@Data
@ToString
public class ReplyReplyVo {
    private Comment reply;//评论的回复
    private User toUser;//被回复的对象
    private User replyUser;//回复评论的用户
}