package com.nowcoder.community.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/2:33
 * @Description: 用户账号修改服务
 */
public interface UserSettingService {
    /**
    * @Description: 修改用户的头像地址，返回影响的行数
    * @Param: [userId, imagePath]
    * @return: [int, java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/4
    */
    int updateUserHeaderImage(int userId,String imagePath);
    /**
    * @Description: 修改密码
    * @Param: [oldPasswd, newPasswd]
    * @return: [java.lang.String, java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/4
    */
    Map<String,String> modifyPassword(int userId, String oldPasswd,String newPasswd);
}
