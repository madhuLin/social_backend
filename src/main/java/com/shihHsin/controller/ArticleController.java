package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.ArticleDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Article;
import com.shihHsin.pojo.ArticleChain;
import com.shihHsin.service.IArticleChainService;
import com.shihHsin.service.IArticleService;
import com.shihHsin.service.IIpfsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */

@Slf4j
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    public IArticleService articleServer;
    @Resource
    public IArticleChainService articleChainService;
    @Resource
    private IIpfsService ipfsService;

    @RequestMapping("/list")
    public R get() {
        log.debug("debugAAsadas");
        List<Article> articleList = articleServer.list();
        return R.success(articleList);
    }

    @RequestMapping("/chainInfo/{id}")
    public R getArticleChain(@PathVariable("id") Integer id) {
        log.debug("debug:getById"+  id.toString());
        LambdaQueryWrapper<ArticleChain> wrapper = new LambdaQueryWrapper<>();
        ArticleChain articleChain = articleChainService.getOne(wrapper.eq(ArticleChain::getArticleId, id));
        return R.success(articleChain);
    }


//    @RequestMapping("/uploadIPFS")
//    public R loadIPFS(String content) {
////        try {
////            ObjectMapper objectMapper = new ObjectMapper();
////            String articleJson = objectMapper.writeValueAsString(article);
////            log.debug("Converted article to JSON: {}", articleJson);
////            byte[] data = articleJson.getBytes();
////            ipfsHash = ipfsService.uploadToIpfs(data);
////        }
////        catch (Exception e) {
////            log.error("Failed to convert article to JSON", e);
////            return R.error("Failed to convert article to JSON");
////        }
////        if(ipfsHash == null) { // upload to IPFS failed
////            return R.error("Failed to upload article to IPFS");
////        }
//
//    }


    @RequestMapping("/uploadIPFS")
    public R loadIPFS(@RequestBody Map<String, Object> requestBody) {
        String content = (String) requestBody.get("content");
        log.debug("Received article content: {}", content);
        String ipfsHash = null;
        try {
            byte[] data = content.getBytes();
            ipfsHash = ipfsService.uploadToIpfs(data);
        } catch (Exception e) {
            log.error("Failed to upload article to IPFS", e);
            return R.error("Failed to upload article to IPFS");
        }
        return R.success(ipfsHash);
    }

    /**
     * 上傳
     * @param article Data
     * @return R
     */
    @PostMapping("/upload")
    public R upload(@RequestBody ArticleDto article, HttpSession session) {
        log.debug("Received article data: {}", article.toString());
        try {
            Article articleEntity = new Article();
            articleEntity.setTitle(article.getTitle());
            articleEntity.setContent(article.getContent());
            articleEntity.setAuthorName(article.getUsername());
            articleEntity.setAuthorAddress(article.getAddress());
            Timestamp timestamp = new Timestamp(article.getPublicationDate());
            articleEntity.setPublicationDate(timestamp);
            articleEntity.setChained(article.isChained());
            articleEntity.setState(true);
            articleServer.save(articleEntity);
            Integer articleId = articleEntity.getId();
            if(article.isChained()) {
                ArticleChain articleChain = new ArticleChain();
                articleChain.setArticleId(articleId);
                articleChain.setAuthorAddress(article.getAddress());
                articleChain.setTransactionHash(article.getTransactionHash());
                articleChain.setTitle(article.getTitle());
                articleChain.setTimestamp(article.getPublicationDate());
                articleChainService.save(articleChain);
            }

            return R.success("成功發布文章");
        } catch (Exception e) {
            log.error("Failed to upload article", e);
            return R.error("Failed to upload article");
        }

    }




}