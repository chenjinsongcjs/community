package com.nowcoder.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/21:46
 * @Description: 用户类
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private Integer type;//1 超级管理员 2 版主
    private Integer status;//0 未激活，1 已激活
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}