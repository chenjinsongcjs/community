package com.nowcoder.community.service.impl;

import com.nowcoder.community.constant.TicketStatusConstant;
import com.nowcoder.community.dao.LoginTicketDao;
import com.nowcoder.community.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/0:36
 * @Description:
 */
@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private LoginTicketDao loginTicketDao;

    @Override
    public void logout(String ticket) {
        loginTicketDao.updateStatusByTicket(ticket, TicketStatusConstant.TICKET_STATUS_INVALID.getCode());
    }
}