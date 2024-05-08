package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ArticleChainMapper;
import com.shihHsin.pojo.ArticleChain;
import com.shihHsin.service.IArticleChainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArticleChainServiceImpl extends ServiceImpl<ArticleChainMapper, ArticleChain> implements IArticleChainService {
//    @Resource
//    private ArticleChainMapper articleChainMapper;
}
