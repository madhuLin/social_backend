package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ArticleMapper;
import com.shihHsin.mapper.LoveMapper;
import com.shihHsin.pojo.Love;
import com.shihHsin.service.ILoveService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LoveServiceImpl extends ServiceImpl<LoveMapper, Love> implements ILoveService {
    @Resource
    private LoveMapper LoveMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public boolean isArticleLoveByUser(Integer articleId, Integer userId) {
        QueryWrapper<Love> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId).eq("user_id", userId);
        return LoveMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean addLove(Integer userId, Integer articleId) {
        try {
            Love Love = new Love();
            Love.setUserId(userId);
            Love.setArticleId(articleId);
            int result = LoveMapper.insert(Love);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error adding Love", e);
            return false;
        }
    }

    @Override
    public boolean removeLove(Integer userId, Integer articleId) {
        try {
            QueryWrapper<Love> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("article_id", articleId);
            int result = LoveMapper.delete(queryWrapper);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error removing Love", e);
            return false;
        }
    }

//    @Override
//    public void updateLoveCount(Integer articleId, int increment) {
//        if (increment == 1) {
//            articleMapper.incrementLikeCount(articleId);
//        } else if (increment == -1) {
//            articleMapper.decrementLikeCount(articleId);
//        }
//    }
}
