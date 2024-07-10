package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Bookmark;

public interface IBookmarkService extends IService<Bookmark> {
    boolean isArticleBookmarkedByUser(Integer articleId, Integer userId);
    public boolean addFavorite(Integer userId, Integer articleId);
    public boolean removeFavorite(Integer userId, Integer articleId);
}
