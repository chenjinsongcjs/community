package com.nowcoder.community.domain;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/08/22:34
 * @Description: 点赞、评论、关注的事件对象，当发生这几个事件时将事件对象发送到消息队列中，
 * 之后慢慢将消息发送给目标对象
 */
@Getter
@ToString
public class Event {
    private String topic;//主体，用于标识消息的类型
    private int entityType;
    private int entityId;//发生事件的对象
    private int userId;//触发事件的对象，一般是当前的登录用户
    private int entityUserId;//发生事件对象的作者
    //用于存储一些额为的字段，方便后期的扩展
    private Map<String,Object> data = new HashMap<>();
    //返回当前对象方便链式调用
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}