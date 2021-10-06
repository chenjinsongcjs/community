package com.nowcoder.community.constant;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/06/2:37
 * @Description:
 */
@Getter
public enum MessageStatus {
    MESSAGE_STATUS_UNREAD(0,"未读"),
    MESSAGE_STATUS_READ(1,"已读"),
    MESSAGE_STATUS_DELETE(2,"删除");

    int code;
    String msg;

    MessageStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
