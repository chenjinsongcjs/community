package com.nowcoder.community;

import cn.hutool.core.util.IdUtil;
import com.nowcoder.community.dao.LoginTicketDao;
import com.nowcoder.community.domain.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/22:38
 * @Description:
 */
@SpringBootTest
public class LoginTicketDaoTest {
    @Autowired
    private LoginTicketDao loginTicketDao;

    @Test
    void testSave(){
        LoginTicket ticket = new LoginTicket(null,150, IdUtil.simpleUUID(),0,new Date(System.currentTimeMillis()+1000*60*10));
        int i = loginTicketDao.saveLoginTicket(ticket);
        System.out.println(i);
    }
    @Test
    void testGet(){
        LoginTicket ticket = loginTicketDao.getLoginTicketByTicket("ab5ad23e46ad49e0bb24d0cc42cc9a69");
        System.out.println(ticket);
    }
    @Test
    void testUpdate(){
        loginTicketDao.updateStatusByTicket("ab5ad23e46ad49e0bb24d0cc42cc9a69",1);
    }
}