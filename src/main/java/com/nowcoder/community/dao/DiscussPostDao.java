package com.nowcoder.community.dao;

import com.nowcoder.community.domain.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

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
}
