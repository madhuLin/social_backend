package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Dislike;
import org.apache.ibatis.annotations.Select;

public interface DislikeMapper extends BaseMapper<Dislike> {

    @Select("SELECT COUNT(*) > 0 FROM dislike WHERE user_id = #{userId} AND article_id = #{articleId}")
    boolean existsByUserIdAndDislikePostId(Integer userId, Integer dislikePostId);
}
