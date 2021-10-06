package com.nowcoder.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/2:34
 * @Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String conversationId;//会话id
    private String content;
    private int status;//0 未读 1 已读 2 删除
    private Date createTime;
}