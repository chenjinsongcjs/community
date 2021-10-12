package com.nowcoder.community.service;

import com.nowcoder.community.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/8:23
 * @Description:
 */
public interface UserService extends UserDetailsService {

    User getUserById(int id);
    List<User> getUserByIdBatch(List<Integer> ids);
    User getUserByName(String username);
    User getUserByEmail(String email);
    int saveUser(User user);
    int updateUser(User user);
    int updateUserHeaderImage(int userId,String path);
}
