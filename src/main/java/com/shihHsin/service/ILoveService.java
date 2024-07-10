package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Love;

public interface ILoveService extends IService <Love> {
    boolean isArticleLoveByUser(Integer userId, Integer articleId);
    boolean removeLove(Integer userId, Integer articleId);

    boolean addLove(Integer userId, Integer articleId);
}
