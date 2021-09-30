package com.nowcoder.community;

import com.github.pagehelper.PageHelper;
import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.domain.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/23:37
 * @Description:
 */
@SpringBootTest
public class DiscussPostTest {
    @Autowired
    private DiscussPostDao discussPostDao;
    @Test
    void testGetAllDiscussPosts(){
        PageHelper.startPage(0,10);
        List<DiscussPost> allDiscussPosts = discussPostDao.getAllDiscussPosts();
        for (DiscussPost post : allDiscussPosts) {
            System.out.println(post);
        }
    }
    @Test
    void testGetCount(){
        System.out.println(discussPostDao.getDiscussPostCount());
    }
}