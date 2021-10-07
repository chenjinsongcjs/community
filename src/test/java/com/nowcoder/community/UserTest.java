package com.nowcoder.community;

import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
class UserTest {
    @Autowired
    private UserDao userDao;
    @Test
    void testGetUserByName(){
        User test1 = userDao.getUserByName("test1");
        System.out.println(test1);
    }
    @Test
    void testGetUserById(){
        User userById = userDao.getUserById(11);
        System.out.println(userById);
    }
    @Test
    void testSaveUser(){
        User user = new User(null,"张三","123456","xxx","123@qq.com",1,1,"1","xxx",new Date());
        int row = userDao.saveUser(user);
        System.out.println(user);
    }
    @Test
    void  testUpdate(){
//        User user = new User(152,"李四","123456","xxx","123@qq.com",1,1,"1","xxx",new Date());
        User user = new User();
        user.setId(152);
        user.setUsername("李四");
        userDao.updateUser(user);
    }
    @Test
    void testDeleteUserById(){
        System.out.println(userDao.deleteUserById(152));
    }

    @Test
    void testBatch(){
        List<User> users = userDao.getUserByIdBatch(Arrays.asList(111, 112, 157));
        for (User user : users) {
            System.out.println(user);
        }
    }

}
