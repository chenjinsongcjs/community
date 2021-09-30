package com.nowcoder.community.vo;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/01/0:11
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPage {
    private PageInfo<DiscussPost> pageInfo;
    private List<DiscussPostAndUser> pageRecords;
}