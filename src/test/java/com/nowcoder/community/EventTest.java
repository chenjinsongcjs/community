package com.nowcoder.community;

import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.kafka.KafKaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/08/23:14
 * @Description:
 */
@SpringBootTest
public class EventTest {
    @Autowired
    private KafKaProducer kafKaProducer;
    @Test
    void testProducer(){
        Event event = new Event();
        event.setTopic("like")
                .setEntityId(10)
                .setEntityType(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode())
                .setUserId(157)
                .setEntityUserId(111);
        kafKaProducer.fireEvent(event) ;
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}