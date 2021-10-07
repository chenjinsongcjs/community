package com.nowcoder.community.vo;

import com.nowcoder.community.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/23:10
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserVo {
    private User user;
    private Date followTime;
    private boolean followStatus;
}