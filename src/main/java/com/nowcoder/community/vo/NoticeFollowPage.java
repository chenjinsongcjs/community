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
 * @Date: 2021/10/09/4:37
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeFollowPage {
    PageInfo<Message> pageInfo;
    List<NoticeFollowVo> noticeFollowVos;
}