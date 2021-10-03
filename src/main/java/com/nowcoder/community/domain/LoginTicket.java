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
 * @Date: 2021/10/03/22:26
 * @Description: 登录令牌实体类，，用于代替session
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginTicket {
    private Integer id;
    private Integer userId;
    private String ticket;//登录凭证
    private Integer status;//0 有效 1 失效
    private Date expired; //过期时间
}