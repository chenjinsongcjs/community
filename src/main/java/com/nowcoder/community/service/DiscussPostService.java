package com.nowcoder.community.service;

import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/23:46
 * @Description:
 */
public interface DiscussPostService {
    //分页获取帖子
    MyPage getDiscussPostByPage(int pageNum, int pageSize);
}
