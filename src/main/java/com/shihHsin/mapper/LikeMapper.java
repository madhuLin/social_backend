package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Like;
import org.apache.ibatis.annotations.Select;

//@Mapper
public interface LikeMapper extends BaseMapper<Like> {
    @Select("SELECT COUNT(*) > 0 FROM likes WHERE user_id = #{userId} AND comment_id = #{commentId}")
    boolean existsByUserIdAndCommentId(Integer userId, Integer commentId);
}
