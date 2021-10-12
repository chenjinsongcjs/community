package com.nowcoder.community.dao.es;

import com.nowcoder.community.domain.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/3:16
 * @Description: 参数说明：DiscussPost(存储数据的类型) ，Integer(每个数据的索引类型)
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {
}