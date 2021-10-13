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

    //关注与取消关注  可以关注人帖子或者话题等等
    //被关注的对象实体
    //followee:entityType:entityId -> zset(userId,now) //有序集合，score为时间，记录关注时间
    private static final String PREFIX_FOLLOWEE = "followee";
    //自己作为粉丝，关注了那些
    //follower:userId:entityType -> zset(entityId,now);
    private static final String PREFIX_FOLLOWER = "follower";
    //redis存储验证码的前缀，
    //kaptcha:UUID -> kaptcha //uuid唯一标识一个用户，用户验证码获取，因为此时用户还没有确定是谁
    private static final String PREFIX_KAPTCHA = "kaptcha";
    //登录凭证的前缀 用户Cookie里没有userid
    //login:ticket:ticket -> loginTicket
    private static final String PREFIX_LOGIN_TICKET = "login:ticket";
    //缓存user的前缀
    //user:userId ->user
    private static final String PREFIX_USER = "user";
    //独立访客统计
    //uv:yyyyMMdd -> ip
    private static final String PREFIX_UV = "uv";
    //日活跃用户统计
    //dau:yyyyMMdd -> userId
    private static final String PREFIX_DAU = "dau";

    public static String getLikeKey(int entityType, int entityId) {
        return PREFIX_LIKE + SEPARATOR + entityType + SEPARATOR + entityId;
    }

    public static String getLikeUserKey(int userId) {
        return PREFIX_LIKE_USER + SEPARATOR + userId;
    }

    public static String getFolloweeKey(int entityType, int entityId) {
        return PREFIX_FOLLOWEE + SEPARATOR + entityType + SEPARATOR + entityId;
    }

    public static String getFollowerKey(int userId, int entityType) {
        return PREFIX_FOLLOWER + SEPARATOR + userId + SEPARATOR + entityType;
    }

    public static String getKaptchaKey(String uuid) {
        return PREFIX_KAPTCHA + SEPARATOR + uuid;
    }

    public static String getLoginTicketKey(String ticket) {
        return PREFIX_LOGIN_TICKET + SEPARATOR + ticket;
    }

    public static String getUserKey(int userId) {
        return PREFIX_USER + SEPARATOR + userId;
    }

    public static String getUVKey(String date) {
        return PREFIX_UV + SEPARATOR + date;
    }

    public static String getDAUKey(String date) {
        return PREFIX_DAU + SEPARATOR + date;
    }

    public static String getUnionUVKey(String start, String end) {
        return PREFIX_UV + SEPARATOR + start + SEPARATOR + end;
    }

    public static String getUnionDAUKey(String start, String end) {
        return PREFIX_DAU + SEPARATOR + start + SEPARATOR + end;
    }

    public static String getPostRefreshKey(){
        return "post:refresh";
    }
}