package com.nowcoder.community.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/23:47
 * @Description:
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostDao discussPostDao;
    //获取用户信息注入
    @Autowired
    private UserDao userDao;
    @Override
    public MyPage getDiscussPostByPage(int pageNum, int pageSize) {
        Page<DiscussPost> page = PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> discussPosts = discussPostDao.getAllDiscussPosts();
        List<DiscussPostAndUser> discussPostAndUsers = discussPosts.stream().map((obj) -> {
            DiscussPostAndUser discussPostAndUser = new DiscussPostAndUser();
            if(obj.getUserId() != null){
                User user = userDao.getUserById(obj.getUserId());
                discussPostAndUser.setUser(user);
            }
            discussPostAndUser.setDiscussPost(obj);
            return discussPostAndUser;
        }).collect(Collectors.toList());
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(page, PageConstant.NAVIGATE_PAGES);
        return new MyPage(pageInfo,discussPostAndUsers);
    }
}