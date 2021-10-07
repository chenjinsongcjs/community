package com.nowcoder.community.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/5:49
 * @Description: 对redis所有的key进行统一管理
 */
public class RedisKeyUtils {
    private static final String SEPARATOR = ":";
    //点赞存储的前缀 可以对帖子和评论进行点赞，表示那些用户对那个类型的对象点赞
    // like:entityType:entityId -> set(userId)
    private static final String PREFIX_LIKE = "like";
    //用于统一某个用户获取的总赞数
    //like:user:userId ->number
    private static final String PREFIX_LIKE_USER = "like:user";

    public static String getLikeKey(int entityType, int entityId) {
        return PREFIX_LIKE + SEPARATOR + entityType + SEPARATOR + entityId;
    }

    public static String getLikeUserKey(int userId) {
        return PREFIX_LIKE_USER + SEPARATOR + userId;
    }

}