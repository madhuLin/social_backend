package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Like;
import org.apache.ibatis.annotations.Select;

public interface LikeMapper extends BaseMapper<Like> {
    @Select("SELECT COUNT(*) > 0 FROM like WHERE user_id = #{userId} AND article_id = #{articleId}")
    boolean existsByUserIdAndArticleId(Integer userId, Integer articleId);
}
