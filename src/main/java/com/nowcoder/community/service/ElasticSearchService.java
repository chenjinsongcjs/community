package com.nowcoder.community.service;

import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.vo.MyPage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/2:51
 * @Description:
 */
public interface ElasticSearchService {
    //es的增删改查
    void save(DiscussPost discussPost);
    void delete(int id);
    MyPage searchByPage(String key,int pageNum,int pageSize);

}