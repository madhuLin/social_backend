package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.BookmarkMapper;
import com.shihHsin.pojo.Bookmark;
import com.shihHsin.service.IBookmarkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BookmarkServiceImpl extends ServiceImpl<BookmarkMapper, Bookmark> implements IBookmarkService {

    @Resource
    private BookmarkMapper favoriteMapper;

    public boolean isArticleBookmarkedByUser(Integer articleId, Integer userId) {
        QueryWrapper<Bookmark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId).eq("user_id", userId);
        return favoriteMapper.selectCount(queryWrapper) > 0;
    }
    public boolean addFavorite(Integer userId, Integer articleId) {
        try {
            Bookmark favorite = new Bookmark();
            favorite.setUserId(userId);  // 修改為標準的駝峰命名
            favorite.setArticleId(articleId);  // 修改為標準的駝峰命名
            int result = favoriteMapper.insert(favorite);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error adding favorite", e);
            return false;
        }
    }

    public boolean removeFavorite(Integer userId, Integer articleId) {
        try {
            QueryWrapper<Bookmark> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("article_id", articleId);
            int result = favoriteMapper.delete(queryWrapper);
            return result > 0;
        } catch (Exception e) {
//            logger.error("Error removing favorite", e);
            return false;
        }
    }
}
