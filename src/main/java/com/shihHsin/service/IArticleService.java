package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Article;
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

    void updateLoveCount(Integer articleId, int i);
}
