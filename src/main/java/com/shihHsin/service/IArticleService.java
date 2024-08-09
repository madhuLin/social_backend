package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Article;

import java.util.List;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
public interface IArticleService extends IService<Article> {
    void updateCommentCount(Integer articleId);

    void updateFavoriteCount(Integer articleId, int i);

    void updateLikeCount(Integer articleId, int i);

    List<Article> getByBoardId(Integer boardId);

    void setchained(Integer id);

    String getTitleById(int articleId);
}
