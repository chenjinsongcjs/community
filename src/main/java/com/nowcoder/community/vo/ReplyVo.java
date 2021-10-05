package com.nowcoder.community.vo;

import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.User;
import lombok.Data;
import lombok.ToString;

import java.lang.annotation.Target;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:40
 * @Description: 评论回复VO
  */
@Data
@ToString
public class ReplyVo {
    private Comment reply;//评论的回复
    private User replyUser;//回复评论的用户
    private User replyReplyUser;
//    private List<ReplyReplyVo> replyReplyVos;

}