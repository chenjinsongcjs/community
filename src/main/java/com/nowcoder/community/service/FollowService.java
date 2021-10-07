package com.nowcoder.community.service;

import com.nowcoder.community.dto.FollowerDto;
import com.nowcoder.community.vo.FollowPageVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/21:42
 * @Description: 关注与取消关注服务
 */
public interface FollowService {
    //只有当前登录用户才能关注
    /**
    * @Description: 关注功能
    * @Param: [entityType(被关注的类型), entityId(关注的对象id)]
    * @return: [int, int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    void follow(int entityType,int entityId);
    //取消关注
    /**
    * @Description: 取消关注
    * @Param: [entityType, entityId]
    * @return: [int, int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    void unfollow(int entityType,int entityId);

    /**
    * @Description: 查询我关注的对象，分页处理
    * @Param: [entityType]
    * @return: [int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    FollowPageVo getFolloweeOfMine(int entityType,int userId, int pageNum, int pageSize);
    /**
    * @Description: /查询关注我的对象
    * @Param: [entityType]
    * @return: [int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    FollowPageVo getFollowerOfMine(int entityType,int userId,int pageNum,int pageSize);
    /**
    * @Description: 查看当前用户对某个实体的关注状态
    * @Param: [entityType, entityId]
    * @return: [int, int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    boolean followStatus(int entityType,int entityId);
    /**
    * @Description: 粉丝的数量
    * @Param: [entityType]
    * @return: [int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    long followerCount(int entityType,int userId);
    /**
    * @Description: 关注的实体的数量
    * @Param: [entityType]
    * @return: [int]
    * @Author: 陈进松
    * @Date: 2021/10/7
    */
    long followeeCount(int entityType,int userId);
}
