package com.nowcoder.community.constant;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:54
 * @Description:
 */
@Getter
public enum CommentConstant {
    ENTITY_TYPE_DISCUSS_POST(1,"帖子的评论"),
    ENTITY_TYPE_COMMENT(2,"评论的回复");
    int code ;
    String msg;

    CommentConstant(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}