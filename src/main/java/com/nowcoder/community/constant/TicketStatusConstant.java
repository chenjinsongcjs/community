package com.nowcoder.community.constant;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/23:01
 * @Description:
 */
@Getter
public enum TicketStatusConstant {
    TICKET_STATUS_EFFECTIVE(0,"有效"),
    TICKET_STATUS_INVALID(1,"无效");
    int code;
    String msg;
    TicketStatusConstant(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}