package com.nowcoder.community.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.es.DiscussPostRepository;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/2:52
 * @Description:
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    private DiscussPostRepository repository;
    @Autowired
    private ElasticsearchRestTemplate template;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;

    @Override
    public void save(DiscussPost discussPost) {
        repository.save(discussPost);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public MyPage searchByPage(String key,int pageNum, int pageSize) {
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(key,"title","content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(pageNum-1,pageSize))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        org.springframework.data.domain.Page<DiscussPost> pageES = template.queryForPage(query, DiscussPost.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                SearchHits hits = response.getHits();
                if (hits.getTotalHits() <= 0)
                    return null;
                List<DiscussPost> posts = new ArrayList<>();
                for (SearchHit hit : hits) {
                    DiscussPost post = new DiscussPost();
                    String id = hit.getSourceAsMap().get("id").toString();
                    post.setId(Integer.valueOf(id));

                    String userId = hit.getSourceAsMap().get("userId").toString();
                    post.setUserId(Integer.valueOf(userId));

                    String title = hit.getSourceAsMap().get("title").toString();
                    post.setTitle(title);

                    String content = hit.getSourceAsMap().get("content").toString();
                    post.setContent(content);

                    String status = hit.getSourceAsMap().get("status").toString();
                    post.setStatus(Integer.valueOf(status));

                    String createTime = hit.getSourceAsMap().get("createTime").toString();
                    try {
                        post.setCreateTime(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime).getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
                    post.setCommentCount(Integer.valueOf(commentCount));
                    //处理高亮
                    HighlightField title1 = hit.getHighlightFields().get("title");
                    if (title1 != null)
                        post.setTitle(title1.getFragments()[0].toString());
                    HighlightField content1 = hit.getHighlightFields().get("content");
                    if (content1 != null)
                        post.setContent(content1.getFragments()[0].toString());
                    posts.add(post);
                }
                return new AggregatedPageImpl(posts, pageable,
                        hits.getTotalHits(),
                        response.getAggregations(),
                        response.getScrollId(),
                        hits.getMaxScore());
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });
        Page<DiscussPost> page = new Page<>();
        //page转换
        page.setPageNum(pageES.getNumber());
        page.setPageSize(pageES.getSize());
        page.setPages(pageES.getTotalPages());
        page.setTotal(pageES.getTotalElements());



        page.addAll(pageES.get().collect(Collectors.toList()));
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(page, PageConstant.NAVIGATE_PAGES);
        List<DiscussPostAndUser> postAndUsers = pageES.get().map(discussPost -> {
            DiscussPostAndUser postAndUser = new DiscussPostAndUser();
            postAndUser.setDiscussPost(discussPost);
            postAndUser.setUser(userService.getUserById(discussPost.getUserId()));
            postAndUser.setLikeCount(likeService.getLikeCount(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), discussPost.getId()));
            return postAndUser;
        }).collect(Collectors.toList());

        return new MyPage(pageInfo,postAndUsers);
    }
}