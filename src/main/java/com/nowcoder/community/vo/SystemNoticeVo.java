package com.nowcoder.community.vo;

import lombok.Data;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/09/1:05
 * @Description:
 */
@Data
@ToString
public class SystemNoticeVo {
    private NoticeLikeVo noticeLikeVo;
    private NoticeCommentVo noticeCommentVo;
    private NoticeFollowVo noticeFollowVo;
    private long allUnreadNoticeCount;
}