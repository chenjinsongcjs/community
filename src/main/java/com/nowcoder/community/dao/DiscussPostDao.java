package com.nowcoder.community.dao;

import com.nowcoder.community.domain.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/23:17
 * @Description:
 */
@Mapper
public interface DiscussPostDao {
    //查询首页显示的的帖子
    List<DiscussPost> getAllDiscussPosts(@Param("orderModel") int orderModel);
    //查询帖子的数量
    int getDiscussPostCount();
    //插入帖子
    int saveDiscussPost(DiscussPost discussPost);

    //根据id查询帖子用于显示帖子的详细信息
    DiscussPost getDiscussPostById(@Param("id") int id);
    //更新帖子的评论数
    int updateCommentCount(@Param("id") int id);
    //实现置顶加精和删除功能
    //置顶修改帖子的类型
    int updateTypeOfPost(@Param("id") int id,@Param("type") int type);
    //加精和删除功能修改帖子的状态
    int updateStatusOfPost(@Param("id") int id,@Param("status") int status);

    int updateScore(@Param("postId") int postId, @Param("score") double score);
}
