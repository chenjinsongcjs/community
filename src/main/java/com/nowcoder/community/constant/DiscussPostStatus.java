package com.nowcoder.community.constant;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/22:06
 * @Description:
 */
@Getter
public enum DiscussPostStatus {
    //三种状态普通帖子，精华帖子 ，被删除的帖子
    POST_STATUS_ORDINARY(0,"普通帖子"),
    POST_STATUS_WONDERFUL(1,"精华帖子"),
    POST_STATUS_DELETE(2,"被删除的帖子");

    int code;
    String msg;

    DiscussPostStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
