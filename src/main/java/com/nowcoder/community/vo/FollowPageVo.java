package com.nowcoder.community.vo;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/23:08
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowPageVo {
    private PageInfo<User> pageInfo;
    private List<FollowUserVo> followUserVos;
}