package com.nowcoder.community.dao;

import com.nowcoder.community.domain.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/22:29
 * @Description:
 */
@Mapper
public interface LoginTicketDao {
    //使用注解方式进行SQL注入，在SQL简单的时候可以使用
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")//id回填
    int saveLoginTicket(LoginTicket loginTicket);
    @Select({
            "select id,user_id,ticket,status,expired from login_ticket ",
            "where ticket = #{ticket}"
    })
    LoginTicket getLoginTicketByTicket(String ticket);
    @Update({
            "update login_ticket set status = #{status} ",
            "where ticket = #{ticket}"
    })
    int updateStatusByTicket(String ticket,int status);
}
