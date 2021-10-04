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
 * @Date: 2021/09/30/21:52
 * @Description: 帖子类
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPost {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Integer type;//0 普通 1 置顶
    private Integer status;//0 正常 1 精华
    private Date createTime;
    private Integer commentCount;
    private Double score;//排名分数
}