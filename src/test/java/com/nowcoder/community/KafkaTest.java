package com.nowcoder.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/08/7:39
 * @Description:
 */
@SpringBootTest
public class KafkaTest {
    @Autowired
    private KafkaTemplate template;

    @Test
    public void test(){
        template.send("test","第一条数据");
        try {
            TimeUnit.SECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
@Component
class Consumer{
    @KafkaListener(topics = {"test"})
    public void handle(ConsumerRecord record){
        System.out.println(record.value());
    }
}