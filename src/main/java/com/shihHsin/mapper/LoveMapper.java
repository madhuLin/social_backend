package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Love;
import org.apache.ibatis.annotations.Select;

public interface LoveMapper extends BaseMapper<Love> {
    @Select("SELECT COUNT(*) > 0 FROM love WHERE user_id = #{userId} AND article_id = #{articleId}")
    boolean existsByUserIdAndArticleId(Integer userId, Integer commentId);
}
