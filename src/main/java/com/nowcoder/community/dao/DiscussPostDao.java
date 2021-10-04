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
    List<DiscussPost> getAllDiscussPosts();
    //查询帖子的数量
    int getDiscussPostCount();
    //插入帖子
    int saveDiscussPost(DiscussPost discussPost);

    //根据id查询帖子用于显示帖子的详细信息
    DiscussPost getDiscussPostById(@Param("id") int id);
}
