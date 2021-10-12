package com.nowcoder.community.service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/0:10
 * @Description: 网站统计服务，对网站的日活跃用户，或者访客进行统计
 */
public interface StatisticalService {
    //一般都是区间统计
    //添加访客
    void addUV(Date date, String ip);
    //添加活跃用户
    void addDAU(Date date,int userId);
    //进行区间统计
    long intervalUV(Date start,Date end);
    long intervalDAU(Date start,Date end);

}
