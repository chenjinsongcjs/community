package com.nowcoder.community;

import com.nowcoder.community.service.LikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/6:27
 * @Description:
 */
@SpringBootTest
public class LikeServiceTest {
    @Autowired
    private LikeService likeService;
    @Test
    void testLike(){
        likeService.like(1,10,157,111);
    }
}