package com.nowcoder.community.constant;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/23:21
 * @Description:
 */
@Getter
public enum LoginTicketExpiredTime {
    no_remember_me(60*60*12,"12小时"),
    has_remember_me(60*60*24*100,"一百天");
    int second;
    String msg;

    LoginTicketExpiredTime(int second, String msg) {
        this.second = second;
        this.msg = msg;
    }
}
