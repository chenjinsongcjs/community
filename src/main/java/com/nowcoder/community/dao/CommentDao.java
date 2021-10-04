package com.nowcoder.community.dao;

import com.nowcoder.community.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:12
 * @Description: 评论数据数据持久层
 */
@Mapper
public interface CommentDao {
    //查询帖子所有的评论数据，然后再业务层进行分页，统计数量就是list的size
    @Select({
            "select id,user_id,entity_type,entity_id,target_id,content,status,create_time",
            "from `comment` ",
            "where status = 0 and entity_type = #{entityType} and entity_id = #{entityId} ",
            "order by create_time desc "
    })
    List<Comment> getAllComments(int entityType,int entityId);
}