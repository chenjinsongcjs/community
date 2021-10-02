package com.nowcoder.community.service;

import com.nowcoder.community.domain.User;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/1:18
 * @Description:
 */
public interface RegisterService {
    /**
    * @Description: 用户注册，校验用户数据
    * @Param: [user]
    * @return: [com.nowcoder.community.domain.User]
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    Map<String,String> register(User user);
    /**
    * @Description: 激活账号
    * @Param: [userId, activationCode]
    * @return: [java.lang.Long, java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    int activation(int userId, String activationCode);
}