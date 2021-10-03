package com.nowcoder.community.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.UserSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/2:35
 * @Description:
 */
@Service
@Slf4j
public class UserSettingServiceImpl implements UserSettingService {
    @Autowired
    private UserDao userDao;

    @Override
    public int updateUserHeaderImage(int userId, String imagePath) {
        return userDao.updateUserHeaderImage(userId,imagePath);
    }

    @Override
    public Map<String, String> modifyPassword(int userId,String oldPasswd, String newPasswd) {
        User user = userDao.getUserById(userId);
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(oldPasswd)){
            map.put("passwordMsg","原始密码不能为空");
            return map;
        }
        if(StringUtils.isEmpty(newPasswd)){
            map.put("newPasswordMsg","新密码不能为空");
            return map;
        }
        if(user == null){
            //用户不应该为空
            log.info("用户不存在，系统出现异常");
        }
        //校验原密码是否正确
        oldPasswd = DigestUtil.md5Hex(oldPasswd+user.getSalt());
        if(!oldPasswd.equals(user.getPassword())){
            map.put("passwordMsg","原始密码错误，请重新输入");
            return map;
        }
        //修改密码
        String salt = IdUtil.simpleUUID().substring(0, 5);
        newPasswd = DigestUtil.md5Hex(newPasswd+salt);
        user.setPassword(newPasswd);
        user.setSalt(salt);
        userDao.updateUser(user);
        return null;
    }
}