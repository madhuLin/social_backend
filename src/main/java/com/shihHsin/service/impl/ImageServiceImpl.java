package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ImageMapper;
import com.shihHsin.pojo.Image;
import com.shihHsin.service.IImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends ServiceImpl <ImageMapper, Image> implements IImageService {
}
