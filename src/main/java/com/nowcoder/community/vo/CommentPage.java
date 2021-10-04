package com.nowcoder.community.vo;

import com.github.pagehelper.PageInfo;
import com.nowcoder.community.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/5:03
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPage {
    private PageInfo<Comment> pageInfo;
    private List<CommentVO> pageRecords;

}