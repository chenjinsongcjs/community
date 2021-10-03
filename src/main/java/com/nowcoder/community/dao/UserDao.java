package com.nowcoder.community.dao;

import com.nowcoder.community.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/21:42
 * @Description: 对用户的CRUD
 */
@Mapper
public interface UserDao {
    //根据用户名查询用户
    User getUserByName(@Param("name") String name);
    //根据用户id查询用户
    User getUserById(@Param("id") Integer id);
    //添加用户
    int saveUser(@Param("user") User user);
    //删除用户
    int deleteUserById(@Param("id") Integer id);
    //修改用户信息
    int updateUser(@Param("user") User user);
    //查询邮箱
    User getUserByEmail(@Param("email") String email);
    //修改用户头像
    int updateUserHeaderImage(@Param("userId") int userId, @Param("imagePath") String imagePath);
}
