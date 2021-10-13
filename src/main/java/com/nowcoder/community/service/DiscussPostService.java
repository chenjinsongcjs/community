package com.nowcoder.community.service;

import com.nowcoder.community.domain.DiscussPost;
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
    MyPage getDiscussPostByPage(int pageNum, int pageSize,int orderModel);
    //发布帖子
    String sendDDiscussPost(int userId,String title,String content);
    //获取帖子的详细信息
    DiscussPostAndUser getDiscussPostDetailById(int id);
    //帖子的置顶精华和删除功能
    //置顶
    int topPost(int postId);
    //精华
    int wonderfulPost(int postId);
    //删除帖子
    int deletePost(int postId);
    //根据id获取帖子
    DiscussPost getDiscussPostById(int PostId);
    //更新帖子分数
    int updatePostScore(int postId,double score);
}
