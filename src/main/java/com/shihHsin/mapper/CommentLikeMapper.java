package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.CommentLike;
import org.apache.ibatis.annotations.Select;


public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    @Select("SELECT COUNT(*) > 0 FROM comment_like WHERE user_id = #{userId} AND comment_id = #{commentId}")
    boolean existsByUserIdAndCommentId(Integer userId, Integer commentId);
}
