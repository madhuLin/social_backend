package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Like;

public interface ILikeService extends IService <Like> {
    boolean isArticleLikeByUser(Integer userId, Integer articleId);
    boolean removeLike(Integer userId, Integer articleId);

    boolean addLike(Integer userId, Integer articleId);
    boolean addDislike(Integer userId, Integer articleId);

    boolean isArticleDislikeByUser(Integer id, Integer userId);
}
