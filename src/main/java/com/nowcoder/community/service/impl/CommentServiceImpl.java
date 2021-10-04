package com.nowcoder.community.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.constant.CommentConstant;
import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.dao.CommentDao;
import com.nowcoder.community.dao.UserDao;
import com.nowcoder.community.domain.Comment;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.vo.CommentPage;
import com.nowcoder.community.vo.CommentVO;
import com.nowcoder.community.vo.ReplyReplyVo;
import com.nowcoder.community.vo.ReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;

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
                replyVo.setReplyUser(user1);
                //设置回复的回复
                List<Comment> replyReplys = commentDao.getAllComments(CommentConstant.ENTITY_TYPE_COMMENT.getCode(), reply.getId());
                List<ReplyReplyVo> replyReplyVos = replyReplys.stream().map(replyReply -> {
                    ReplyReplyVo replyReplyVo = new ReplyReplyVo();
                    replyReplyVo.setReply(replyReply);
                    User user2 = userDao.getUserById(replyReply.getUserId());
                    replyReplyVo.setReplyUser(user2);
                    return replyReplyVo;
                }).collect(Collectors.toList());
                //设置回复的回复
                replyVo.setReplyReplyVos(replyReplyVos);
                return replyVo;
            }).collect(Collectors.toList());
            //设置评论的回复
            commentVO.setReplyVos(replyVos);
            return commentVO;
        }).collect(Collectors.toList());


        PageInfo<Comment> pageInfo =
                new PageInfo<>(comments,PageConstant.NAVIGATE_PAGES);//PageConstant.NAVIGATE_PAGES分页栏的大小
        return new CommentPage(pageInfo,commentVOS);
    }
}