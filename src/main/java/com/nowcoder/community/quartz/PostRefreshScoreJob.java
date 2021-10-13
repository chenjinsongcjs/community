package com.nowcoder.community.quartz;

import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.domain.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.utils.RedisKeyUtils;
import com.nowcoder.community.vo.DiscussPostAndUser;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/5:35
 * @Description: 帖子定时刷新分数进行热帖排行
 * <p>
 * 在计算热帖的排行榜的时候不会，一有点赞或评论等操作就去刷新分数，这样的效率较低
 * 可以使用定时器定时刷新帖子的分数
 * 将每次被点赞的帖子ID放入Redis的set中，启动定时任务定时消费Redis中的帖子，进行分数刷新
 * 同时更新es中的数据方便检索
 */
@Slf4j
public class PostRefreshScoreJob implements Job {

    /**
     * log(精华分 + 评论数*10 + 点赞数*2 + 收藏数*2) + (发布时间 – 牛客纪元)
     * 热帖分数的计算
     * 收藏暂时不计算
     */
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Date epoch;

    static {
        try {
            epoch = format.parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("牛客纪元初始化失败", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String postRefreshKey = RedisKeyUtils.getPostRefreshKey();
        BoundSetOperations<String, String> ops = redisTemplate.boundSetOps(postRefreshKey);
        if (ops.size() == null || ops.size() <= 0) {
            //没有任何数据不用处理，取消定时器
            log.info("目前没有任何点赞评论的帖子，定时任务取消");
            return;
        }
        log.info("定时任务开始：{}", format.format(new Date()));
        while (ops.size() > 0) {
            this.refresh(Integer.parseInt(ops.pop()));
        }
        log.info("定时任务结束：{}", format.format(new Date()));
    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.getDiscussPostById(postId);
        if (post == null) {
            log.info("帖子不存在:{}", postId);
            return;
        }
        Integer commentCount = post.getCommentCount();
        long likeCount = likeService.getLikeCount(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), postId);
        boolean isWonderful = post.getStatus() == 1;
        //计算权重
        double w = (isWonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        double score = Math.log10(Math.max(w,1))+(post.getCreateTime().getTime() - epoch.getTime())/(1000 * 3600 * 24)*1.0;
        //更新帖子分数和es中的数据
        discussPostService.updatePostScore(postId,score);
        post.setScore(score);
        elasticSearchService.save(post);
    }
}