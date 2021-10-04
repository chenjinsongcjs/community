package com.nowcoder.community;

import com.nowcoder.community.utils.SensitiveWordsFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/23:14
 * @Description:
 */
@SpringBootTest
public class SensitiveWordsTest {
    @Autowired
    private SensitiveWordsFilter filter;

    @Test
    void testSensitive(){
        String test = "这里可以进行赌博,可以☆嫖☆娼☆,可以吸毒,可以看黄片.o(*￣︶￣*)o";
        System.out.println(filter.filter(test));
    }
}