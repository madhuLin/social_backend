package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ImageMapper;
import com.shihHsin.pojo.Image;
import com.shihHsin.service.IImageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl extends ServiceImpl <ImageMapper, Image> implements IImageService {

    @Resource
    private ImageMapper imageMapper;
    @Override
    public List<Image> findImagesByArticleId(int articleId) {
        return imageMapper.findByArticleId(articleId);
    }
}
