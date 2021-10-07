package com.nowcoder.community.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/6:02
 * @Description: 用于处理点赞业务
 */
public interface LikeService {
    /**
    * @Description: 点赞和取消点赞还有统一当前被点赞的作者的总赞数,点赞丢几个没有关系
    * @Param: [entityType(被点赞的类型), entityId(被点赞的id), userId(点赞者), entityUserId](被点赞的用户)
    * @return: [int, int, int, int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    void like(int entityType,int entityId,int userId,int entityUserId);
    /**
    * @Description: 获取当前点赞状态,其实可以通过下面的数量就可以判断
    * @Param: [entityType, entityId]
    * @return: [int, int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    boolean likeStatus(int entityType, int entityId);
    /**
    * @Description: 获取当前实体被点赞的数量
    * @Param: [entityType, entityId]
    * @return: [int, int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    long getLikeCount(int entityType,int entityId);
    /**
    * @Description: 统计当前用户获取 的所有赞
    * @Param: [userId]
    * @return: [int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    long getAllLikeCountOfUser(int userId);

}