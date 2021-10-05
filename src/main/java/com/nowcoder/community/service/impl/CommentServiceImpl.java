package com.nowcoder.community.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.CommentDao;
import com.nowcoder.community.dao.DiscussPostDao;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.utils.SensitiveWordsFilter;
import com.nowcoder.community.vo.CommentPage;
import com.nowcoder.community.vo.CommentVO;
import com.nowcoder.community.vo.ReplyReplyVo;
import com.nowcoder.community.vo.ReplyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/4:48
 * @Description: 评论数据获取实现类
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SensitiveWordsFilter filter;
    @Autowired
    private DiscussPostDao discussPostDao;
    @Override
    public CommentPage getAllCommentByPage(int entityId, int pageNum) {
        Page<Comment> comments = PageHelper.startPage(pageNum, PageConstant.COMMENT_PAGE_SIZE);
        //查询帖子的评论
        List<Comment> allComments =
                commentDao.getAllComments(CommentConstant.ENTITY_TYPE_DISCUSS_POST.getCode(), entityId);
        //查每一条评论的回复
        List<CommentVO> commentVOS = allComments.stream().map(comment -> {
            CommentVO commentVO = new CommentVO();
            //查询帖子评论用户
            User user = userDao.getUserById(comment.getUserId());
            commentVO.setCommentUser(user);
            commentVO.setComment(comment);
            //所有的评论的回复
            List<Comment> replies = commentDao.getAllComments(CommentConstant.ENTITY_TYPE_COMMENT.getCode(), comment.getId());
            List<ReplyVo> replyVos = replies.stream().map(reply -> {
                ReplyVo replyVo = new ReplyVo();
                replyVo.setReply(reply);
                User user1 = userDao.getUserById(reply.getUserId());
                User user2 = userDao.getUserById(reply.getTargetId());
//                if(user2 == null){
//                    replyVo.setReplyUser(user1);
//                }else{
//                    replyVo.setReplyUser(user2);
//                    replyVo.setReplyReplyUser(user1);
//                }
                replyVo.setReplyUser(user2);
                replyVo.setReplyReplyUser(user1);
                return replyVo;
            }).collect(Collectors.toList());
            //设置评论的回复
            commentVO.setReplyVos(replyVos);
            return commentVO;
        }).collect(Collectors.toList());

//        log.info("查询到的评论对象：{}",commentVOS);
        PageInfo<Comment> pageInfo =
                new PageInfo<>(comments,PageConstant.NAVIGATE_PAGES);//PageConstant.NAVIGATE_PAGES分页栏的大小
        return new CommentPage(pageInfo,commentVOS);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int saveComment(Comment comment) {
        //对评论的内容进行处理
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(filter.filter(comment.getContent()));
        //对帖子的commentCount冗余字段进行处理
        discussPostDao.updateCommentCount(comment.getUserId());
        return commentDao.saveComment(comment);
    }
}