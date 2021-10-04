package com.nowcoder.community;

import com.nowcoder.community.dao.CommentDao;
import com.nowcoder.community.domain.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:26
 * @Description:
 */
@SpringBootTest
public class CommentMapperTest {
    @Autowired
    private CommentDao commentDao;
    @Test
    void testGetAll(){
        List<Comment> allComments = commentDao.getAllComments(1, 228);
        for (Comment allComment : allComments) {
            System.out.println(allComment);
        }
    }
}