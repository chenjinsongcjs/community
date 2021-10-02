package com.nowcoder.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "用户名不能为空!")
    private String username;
    @NotBlank(message = "密码不能为空！")
    @Min(value = 8,message = "密码不能小于8位")
    private String password;
    private String salt;
    @Email(message = "邮箱格式不正确！")
    private String email;
    private Integer type;//1 超级管理员 2 版主
    private Integer status;//0 未激活，1 已激活
    private String activationCode;//激活码
    private String headerUrl;
    private Date createTime;
}