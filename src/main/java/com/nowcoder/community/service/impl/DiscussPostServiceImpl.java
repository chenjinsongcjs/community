package com.nowcoder.community.service.impl;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.*;
import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.domain.Event;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.kafka.KafKaProducer;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.utils.SensitiveWordsFilter;
import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/23:47
 * @Description:
 */
@Service
@Slf4j
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostDao discussPostDao;
    @Autowired
    private SensitiveWordsFilter filter;
    @Autowired
    private LikeService likeService;
    //获取用户信息注入
    @Autowired
    private UserService userService;

    @Autowired
    private KafKaProducer producer;
    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;
    private LoadingCache<String,Page<DiscussPost>> postCache;

    //初始化缓存
    @PostConstruct
    public void init(){
        postCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Page<DiscussPost>>() {
                    @Override
                    public @Nullable Page<DiscussPost> load(String key) throws Exception {
                        if (StringUtils.isEmpty(key)){
                            throw new RuntimeException("参数不能为空");
                        }
                        String[] split = key.split(":");
                        if  (split.length != 2){
                            throw new RuntimeException("参数不正确");
                        }
                        int pageNum = Integer.parseInt(split[0]) ;
                        int pageSize = Integer.parseInt(split[1]);
                        //在这里可以做二级缓存
                        log.info("从数据库中加载数据");
                        Page<DiscussPost> page = PageHelper.startPage(pageNum, pageSize);
                        discussPostDao.getAllDiscussPosts(1);
                        return page;
                    }
                });
    }

    @Override
    public MyPage getDiscussPostByPage(int pageNum, int pageSize,int orderModel) {
        //在这里使用缓存
        Page<DiscussPost> page;
        if (orderModel == 1){
            log.info("从缓存中加载数据");
            page = postCache.get(pageNum + ":" + pageSize);
        }else{
            log.info("压力测试");
            log.info("从数据库中加载数据");
            page = PageHelper.startPage(pageNum, pageSize);
            discussPostDao.getAllDiscussPosts(orderModel);
        }

        List<DiscussPost> discussPosts = page.getResult();
        List<DiscussPostAndUser> discussPostAndUsers = discussPosts.stream().map((obj) -> {
            DiscussPostAndUser discussPostAndUser = new DiscussPostAndUser();
            if(obj.getUserId() != null){
                User user = userService.getUserById(obj.getUserId());
                discussPostAndUser.setUser(user);
            }
            discussPostAndUser.setDiscussPost(obj);
            long likeCount = likeService.getLikeCount(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), obj.getId());
            boolean likeStatus = likeService.likeStatus(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), obj.getId());
            discussPostAndUser.setLikeStatus(likeStatus);
            discussPostAndUser.setLikeCount(likeCount);
            return discussPostAndUser;
        }).collect(Collectors.toList());
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(page, PageConstant.NAVIGATE_PAGES);
        return new MyPage(pageInfo,discussPostAndUsers);
    }

    @Override
    public String sendDDiscussPost(int userId, String title, String content) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content)){
            log.info("发帖出现参数异常");
            return JSONUtils.getJSONString(1,"帖子的标题和内容不能为空");
        }
        //html标签处理
        title = HtmlUtils.htmlEscape(title);
        content = HtmlUtils.htmlEscape(content);
        //敏感词处理
        title = filter.filter(title);
        content = filter.filter(content);
        //帖子发送
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(userId);
        discussPost.setContent(content);
        discussPost.setCommentCount(0);
        discussPost.setScore(0.0);
        discussPost.setStatus(0);
        discussPost.setTitle(title);
        discussPost.setType(0);
        discussPost.setCreateTime(new Date());
        discussPostDao.saveDiscussPost(discussPost);
        //发帖添加到es中 ,异步添加
        Event event = new Event().setTopic(EventConstant.event_publish)
                .setData("postId", discussPost.getId());
        producer.fireEvent(event);

        return JSONUtils.getJSONString(0,"发帖成功");
    }

    @Override
    public DiscussPostAndUser getDiscussPostDetailById(int id) {
        DiscussPost discussPost = discussPostDao.getDiscussPostById(id);
        User user = userService.getUserById(discussPost.getUserId());
        long likeCount = likeService.getLikeCount(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), id);
        boolean likeStatus = likeService.likeStatus(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), id);
        return new DiscussPostAndUser(user,discussPost, likeCount,likeStatus);
    }

    @Override
    public int topPost(int postId) {
        return discussPostDao.updateTypeOfPost(postId, DiscussPostType.POST_TYPE_TOP.getCode());
    }

    @Override
    public int wonderfulPost(int postId) {
        return discussPostDao.updateStatusOfPost(postId,DiscussPostStatus.POST_STATUS_WONDERFUL.getCode()) ;
    }

    @Override
    public int deletePost(int postId) {
        return discussPostDao.updateStatusOfPost(postId,DiscussPostStatus.POST_STATUS_DELETE.getCode());
    }

    @Override
    public DiscussPost getDiscussPostById(int postId) {
        return discussPostDao.getDiscussPostById(postId);
    }

    @Override
    public int updatePostScore(int postId, double score) {
        return discussPostDao.updateScore(postId,score);
    }
}