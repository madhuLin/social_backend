package com.shihHsin.controller;

import com.shihHsin.common.R;
import com.shihHsin.pojo.Article;
import com.shihHsin.service.IArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("")
public class ArticleController {
    @Resource
    public IArticleService articleServer;


    @RequestMapping("/list")
    public R get() {
        log.debug("debugAAsadas");
        List<Article> articleList = articleServer.list();
        return R.success(articleList);
    }
}