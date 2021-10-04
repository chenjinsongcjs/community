package com.nowcoder.community.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:07
 * @Description:
 */
@Data
@ToString
public class Comment {
    private int id;
    private int userId;
    private int entityType;//评论的类型
    private int entityId;//被评论对象的id
    private int targetId;//被回复对象的id
    private String content;
    private int status;
    private Date createTime;
}