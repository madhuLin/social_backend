package com.shihHsin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ArticleMapper extends BaseMapper<Article> {
    @Update("UPDATE article SET comment_count = comment_count + 1 WHERE id = #{articleId}")
    void incrementCommentCount(@Param("articleId") Integer articleId);

    @Update("UPDATE article SET bookmark_count = bookmark_count + 1 WHERE id = #{articleId}")
    void incrementFavoriteCount(Integer articleId);

    @Update("UPDATE article SET bookmark_count = bookmark_count - 1 WHERE id = #{articleId}")
    void decrementFavoriteCount(Integer articleId);

    @Update("UPDATE article SET like_count = like_count + 1 WHERE id = #{articleId}")
    void incrementLikeCount(Integer articleId);

    @Update("UPDATE article SET like_count = like_count - 1 WHERE id = #{articleId}")
    void decrementLikeCount(Integer articleId);
}
