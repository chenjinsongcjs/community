package com.nowcoder.community.service.impl;

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
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.utils.SensitiveWordsFilter;
import com.nowcoder.community.vo.DiscussPostAndUser;
import com.nowcoder.community.vo.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
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

    @Override
    public MyPage getDiscussPostByPage(int pageNum, int pageSize) {
        Page<DiscussPost> page = PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> discussPosts = discussPostDao.getAllDiscussPosts();
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
}