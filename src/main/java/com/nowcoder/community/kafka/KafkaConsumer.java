package com.nowcoder.community.kafka;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.EventConstant;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.domain.Message;
import com.nowcoder.community.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/08/22:51
 * @Description: kafka消费者 ,将接收到消息转换之后放入数据库中
 */
@Slf4j
@Component
public class KafkaConsumer {
    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {EventConstant.event_comment,EventConstant.event_follow,EventConstant.event_like})
    public void consumeAndSaveMessage(ConsumerRecord record){
        log.info("消费者接收到的消息：{}",record.value());
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        //构建消息对象
        Message message = new Message();
        //系统发送消息
        message.setFromId(1);
        //发送给被点赞。。的对象
        message.setToId(event.getEntityUserId());
        //构建一个map作为消息的内容
        Map<String,Object> map = new HashMap<>();
        map.put("entityId",event.getEntityId());
        map.put("entityType",event.getEntityType());
        //不是关注，记录帖子id
        if(!event.getTopic().equals("follow")){
            Map<String, Object> data = event.getData();
            Object postId = data.get("postId");
            map.put("postId",postId);
        }
        //事件触发者
        map.put("userId",event.getUserId());
        message.setContent(JSONObject.toJSONString(map));
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        messageService.saveMessage(message);
        log.info("事件消息接收处理成功");
    }
}