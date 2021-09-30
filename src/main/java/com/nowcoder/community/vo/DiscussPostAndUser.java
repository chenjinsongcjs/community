package com.nowcoder.community.vo;

import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/01/0:43
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPostAndUser {
    private User user;
    private DiscussPost discussPost;
}