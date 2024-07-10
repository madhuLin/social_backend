package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.Dto.CommentDto;
import com.shihHsin.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT c.id, c.article_id, c.user_id, c.content, c.comment_date, c.like_count, u.name as authorName " +
            "FROM comment c " +
            "JOIN user u ON c.user_id = u.id " +
            "WHERE c.article_id = #{articleId}")
    List<CommentDto> getCommentsWithAuthorNameByArticleId(Integer articleId);

    @Select("SELECT c.*, u.name as authorName, " +
            "CASE WHEN l.id IS NOT NULL THEN true ELSE false END as likedByCurrentUser " +
            "FROM comment c " +
            "JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN likes l ON c.id = l.comment_id AND l.user_id = #{userId} " +
            "WHERE c.article_id = #{articleId}")
    List<CommentDto> getCommentsWithAuthorNameAndLikeStatus(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
}
