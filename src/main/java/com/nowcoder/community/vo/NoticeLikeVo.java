package com.nowcoder.community.vo;

import com.nowcoder.community.domain.User;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/09/1:06
 * @Description:
 */
@Data
@ToString
public class NoticeLikeVo {
    private User fromUser;//系统用户
    private long unReadCount;
    private long allNoticeCount;
    private User likeUser;//点赞用户
    private int postId;//帖子id
    private Date createTime;
    private int entityType;//点赞类型
}