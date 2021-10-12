package com.nowcoder.community.constant;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/22:16
 * @Description:
 */
@Getter
public enum DiscussPostType {
    POST_TYPE_ORDINARY(0,"普通帖子"),
    POST_TYPE_TOP(1,"置顶帖子");
    int code;
    String msg;

    DiscussPostType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}