package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ArticleMapper;
import com.shihHsin.pojo.Article;
import com.shihHsin.service.IArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void updateCommentCount(Integer articleId) {
        articleMapper.incrementCommentCount(articleId);
    }

    @Override
    public void updateFavoriteCount(Integer articleId, int i) {
        if(i == 1) articleMapper.incrementFavoriteCount(articleId);
        else articleMapper.decrementFavoriteCount(articleId);
    }
    @Override
    public void updateLoveCount(Integer articleId, int i) {
        if(i == 1) articleMapper.incrementLikeCount(articleId);
        else articleMapper.decrementLikeCount(articleId);
    }

}
