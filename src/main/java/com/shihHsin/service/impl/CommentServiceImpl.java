package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.Dto.CommentDto;
import com.shihHsin.mapper.CommentMapper;
import com.shihHsin.mapper.CommentLikeMapper;
import com.shihHsin.pojo.Comment;
import com.shihHsin.pojo.CommentLike;
import com.shihHsin.service.ICommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CommentLikeMapper commentLikeMapper;

    public List<CommentDto> getCommentsByArticleId(Integer articleId) {
//        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.eq(Comment::getArticleId, articleId)
//                .orderByDesc(Comment::getCommentDate);
//        return commentMapper.selectList(queryWrapper);
        return commentMapper.getCommentsWithAuthorNameByArticleId(articleId);
    }

    public List<CommentDto> getCommentsByArticleId(Integer articleId, Integer userId) {
        List<CommentDto> comments = commentMapper.getCommentsWithAuthorNameByArticleId(articleId);
        // 檢查每個評論是否已被指定的用戶按讚過
        for (CommentDto comment : comments) {
            boolean liked = commentLikeMapper.existsByUserIdAndCommentId(userId, comment.getId());
            comment.setLiked(liked);
        }
        return comments;
    }
    public void addComment(Comment comment) {
        comment.setCommentDate(Timestamp.valueOf(LocalDateTime.now()));
        commentMapper.insert(comment);
    }


    public boolean likeComment(Integer userId, Integer commentId) {
        if (commentLikeMapper.existsByUserIdAndCommentId(userId, commentId)) {
            return false;
        }

        CommentLike like = new CommentLike();
        like.setUserId(userId);
        like.setCommentId(commentId);
        commentLikeMapper.insert(like);

        Comment comment = commentMapper.selectById(commentId);
        if (comment != null) {
            comment.setLikeCount(comment.getLikeCount() + 1);
            commentMapper.updateById(comment);
        }
        return true;
    }
}
