package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ArticleMapper;
import com.shihHsin.pojo.Article;
import com.shihHsin.service.IArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void updateLikeCount(Integer articleId, int i) {
        if(i == 1) articleMapper.incrementLikeCount(articleId);
        else articleMapper.incrementDislikeCount(articleId);
    }

    @Override
    public List<Article> getByBoardId(Integer boardId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getBoardId, boardId);
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public void setchained(Integer articleId) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, articleId).set(Article::isChained, true);
        articleMapper.update(null, updateWrapper);
    }

    @Override
    public String getTitleById(int articleId) {
        Article article = baseMapper.selectById(articleId);
        return article.getTitle();
    }

}
