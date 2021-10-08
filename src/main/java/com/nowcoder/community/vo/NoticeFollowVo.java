package com.nowcoder.community.vo;

import com.nowcoder.community.domain.User;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/09/1:09
 * @Description:
 */
@Data
@ToString
public class NoticeFollowVo {
    private User fromUser;//系统用户
    private long unReadCount;
    private long allNoticeCount;
    private User likeUser;//点赞用户
    private Date createTime;
}