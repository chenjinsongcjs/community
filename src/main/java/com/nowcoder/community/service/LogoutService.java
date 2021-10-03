package com.nowcoder.community.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/0:35
 * @Description:
 */
public interface LogoutService {
    /**
    * @Description: 退出登录，直接让登录令牌失效
    * @Param: [ticket]
    * @return: [java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/4
    */
    void logout(String ticket);
}