package com.nowcoder.community.kafka;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/08/22:46
 * @Description:
 */
@Component
public class KafKaProducer {
    @Autowired
    private KafkaTemplate template;
    /**
    * @Description: 向kafka发送消息触发事件
    * @Param: [event]
    * @return: [com.nowcoder.community.domain.Event]
    * @Author: 陈进松
    * @Date: 2021/10/8
    */
    public void fireEvent(Event event){
        template.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}