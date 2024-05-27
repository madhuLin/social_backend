package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.ArticleDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Article;
import com.shihHsin.pojo.ArticleChain;
import com.shihHsin.pojo.Comment;
import com.shihHsin.pojo.User;
import com.shihHsin.service.*;
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
    public IArticleService articleService;
    @Resource
    public IArticleChainService articleChainService;
    @Resource
    private IIpfsService ipfsService;

    @Resource
    private IUserService userService;

    @Resource
    public ICommentService commentService;

    @RequestMapping("/list")
    public R getArticleList() {
        log.debug("debugAAsadas");
        List<Article> articleList = articleService.list();
        return R.success(articleList);
    }

    @RequestMapping("/chainList")
    public R getChainList() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::isChained, true);
        List<Article> articleList = articleService.list(wrapper);
        return R.success(articleList);
    }

    @RequestMapping("/chainInfo/{id}")
    public R getArticleChain(@PathVariable("id") Integer id) {
        log.debug("debug:getById" + id.toString());
        LambdaQueryWrapper<ArticleChain> wrapper = new LambdaQueryWrapper<>();
        ArticleChain articleChain = articleChainService.getOne(wrapper.eq(ArticleChain::getArticleId, id));
        return R.success(articleChain);
    }

    @RequestMapping("/{id}")
    public R getArticleById(@PathVariable("id") Integer id) {
        log.debug("debug:getById" + id.toString());
//        LambdaQueryWrapper<ArticleChain> wrapper = new LambdaQueryWrapper<>();
        Article article = articleService.getById(id);
        return R.success(article);
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
     *
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
            articleService.save(articleEntity);
            Integer articleId = articleEntity.getId();
            if (article.isChained()) {
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


    /**
     * 文章留言列表
     *
     * @return R
     */
    @RequestMapping("/commentList")
    public R getCommentList() {
        log.debug("debug:getCommentList");
         List<Comment> commentList = commentService.list();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        for(Comment comment : commentList) {
            Integer id = comment.getUser_id();
            userService.getOne(wrapper.eq(User::getId, id));
            User user = userService.getOne(wrapper.eq(User::getId, id));
            comment.setAuthor(user.getName());
        }


        log.debug("debug:getCommentList" + commentList.toString());
        return R.success(commentList);
    }


    /**
     * 新增留言
     *
     * @return R
     */
    @PostMapping("/addComment")
    public R addComment(@RequestBody Comment comment, HttpSession session) {
        log.debug("debug:addComment");

        commentService.save(comment);
        return R.success("留言成功");
    }

}