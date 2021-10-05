package com.nowcoder.community.vo;

import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.User;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:33
 * @Description: 帖子评论数据展示的封装,一条评论的内容
 */
@Data
@ToString
public class CommentVO {
    private Comment comment;//评论内容
    private User CommentUser;//评论用户
    private List<ReplyVo> replyVos;//该条评论的所有回复
}