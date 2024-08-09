package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Image;

import java.util.List;

public interface IImageService extends IService<Image> {
    public List<Image> findImagesByArticleId(int articleId);
}
