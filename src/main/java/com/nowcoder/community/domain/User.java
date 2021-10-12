package com.nowcoder.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
public class User implements UserDetails {
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
    //用户拥有的权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (type){
                    case 0:
                        return "USER";
                    case 1:
                        return "ADMIN";
                    default:
                        return "MODERATOR";
                }
            }
        });
        return list;
    }
    //账户是否没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //账号是否没有锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //账号凭证是否没有过期，，在这里指的是密码
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //账号是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}