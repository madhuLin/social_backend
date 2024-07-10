package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.Dto.CommentDto;
import com.shihHsin.pojo.Comment;

import java.util.List;

public interface ICommentService extends IService<Comment> {
    List<CommentDto> getCommentsByArticleId(Integer articleId);
    List<CommentDto> getCommentsByArticleId(Integer userId, Integer articleId);
    void addComment(Comment comment);

    boolean likeComment(Integer userId, Integer commentId);
}
