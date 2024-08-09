package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ArticleMapper;
import com.shihHsin.mapper.DislikeMapper;
import com.shihHsin.mapper.LikeMapper;
import com.shihHsin.pojo.Dislike;
import com.shihHsin.pojo.Like;
import com.shihHsin.service.ILikeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {
    @Resource
    private LikeMapper LikeMapper;

    @Resource
    private DislikeMapper dislikeMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public boolean isArticleLikeByUser(Integer articleId, Integer userId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId).eq("user_id", userId);
        return LikeMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isArticleDislikeByUser(Integer articleId, Integer userId) {
        QueryWrapper<Dislike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId).eq("user_id", userId);
        return dislikeMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean addLike(Integer userId, Integer articleId) {
        try {
            Like Love = new Like();
            Love.setUserId(userId);
            Love.setArticleId(articleId);
            int result = LikeMapper.insert(Love);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error adding Love", e);
            return false;
        }
    }

    @Override
    public boolean addDislike(Integer userId, Integer articleId) {
        try {
            Dislike dislike = new Dislike();
            dislike.setUserId(userId);
            dislike.setArticleId(articleId);
            int result = dislikeMapper.insert(dislike);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error adding Love", e);
            return false;
        }
    }

    @Override
    public boolean removeLike(Integer userId, Integer articleId) {
        try {
            QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("article_id", articleId);
            int result = LikeMapper.delete(queryWrapper);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error removing Love", e);
            return false;
        }
    }

}
