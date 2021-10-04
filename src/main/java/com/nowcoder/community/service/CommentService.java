package com.nowcoder.community.service;

import com.nowcoder.community.vo.CommentPage;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:31
 * @Description: 评论服务
 */
public interface CommentService {
    /**
    * @Description: 分页返回评论数据,,这个一定是帖子的评论
    * @Param: [entityType(被评论的类型), entityId(被评论的对象id)]
    * @return: [int, int]
    * @Author: 陈进松
    * @Date: 2021/10/5
    */
    CommentPage getAllCommentByPage(int entityId, int pageNum);
}
