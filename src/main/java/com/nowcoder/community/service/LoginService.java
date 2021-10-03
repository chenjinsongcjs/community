package com.nowcoder.community.service;

import com.nowcoder.community.domain.User;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/22:44
 * @Description: 用户登录服务
 */
public interface LoginService {
    /**
    * @Description: 处理用户登录，并进行校验，
    * @Param: [user(用户名和密码校验), expiredSecond(登录凭证的过期时间，单位：秒)]
    * @return: [com.nowcoder.community.domain.User, int]
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    Map<String,String> login(User user,int expiredSecond);
}