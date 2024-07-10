package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.Article2Dto;
import com.shihHsin.Dto.ArticleDto;
import com.shihHsin.Dto.CommentDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Article;
import com.shihHsin.pojo.ArticleChain;
import com.shihHsin.pojo.Comment;
import com.shihHsin.pojo.Image;
import com.shihHsin.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Value("${upload.path}")
    private String uploadPath;

    @Resource
    public IArticleService articleService;

    @Resource
    public IImageService imageService;
    @Resource
    public IArticleChainService articleChainService;
    @Resource
    private IIpfsService ipfsService;

    @Resource
    private IUserService userService;

    @Resource
    public ICommentService commentService;

    @Resource
    public IBookmarkService favoriteService;


    @Resource
    public ILoveService loveService;


    @GetMapping("/list")
    public R getArticleList(@RequestParam(value = "userId", required = false) Integer userId) {
        log.debug("debugAAsadas" + (userId != null ? userId.toString() : "null"));
        List<Article> articleList = articleService.list();
        List<Article2Dto> article2DtoList = new ArrayList<>();
        if (userId == null) return R.success(articleList);
        for (Article article : articleList) {
            boolean isBookmarked = favoriteService.isArticleBookmarkedByUser(article.getId(), userId);
            boolean isLove = loveService.isArticleLoveByUser(article.getId(), userId);
            Article2Dto article2Dto = new Article2Dto(article);
            article2Dto.setBookmarked(isBookmarked);
            article2Dto.setLoved(isLove);
            article2DtoList.add(article2Dto);
        }
        return R.success(article2DtoList);
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

//    @PostMapping("/upload")
//    public R upload(
//            @RequestParam("title") String title,
//            @RequestParam("content") String content,
//            @RequestParam("username") String username,
//            @RequestParam("address") String address,
//            @RequestParam("publicationDate") long publicationDate,
//            @RequestParam("chained") boolean chained,
//            @RequestParam(value = "transactionHash", required = false) String transactionHash,
//            @RequestParam("images") MultipartFile[] images,
//            HttpSession session) {
//
//        log.debug("Received article data: title={}, content={}, username={}, address={}, publicationDate={}, chained={}",
//                title, content, username, address, publicationDate, chained);
//
//        try {
//            Article articleEntity = new Article();
//            articleEntity.setTitle(title);
//            articleEntity.setContent(content);
//            articleEntity.setAuthorName(username);
//            articleEntity.setAuthorAddress(address);
//            Timestamp timestamp = new Timestamp(publicationDate);
//            articleEntity.setPublicationDate(timestamp);
//            articleEntity.setChained(chained);
//            articleEntity.setState(true);
//            articleService.save(articleEntity);
//            Integer articleId = articleEntity.getId();
//
//            // 保存圖片
//            int imageOrder = 1;
//            for (MultipartFile image : images) {
//                if (!image.isEmpty()) {
//                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
//                    Path filePath = Paths.get(uploadPath, fileName);
//                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                    Image img = new Image();
//                    img.setArticleId(articleId);
//                    img.setImagePath(filePath.toString());
//                    img.setImageOrder(imageOrder++);
//                    imageService.save(img);
//                }
//            }
//
//            // 如果文章被鏈接，保存鏈接信息
//            if (chained) {
//                ArticleChain articleChain = new ArticleChain();
//                articleChain.setArticleId(articleId);
//                articleChain.setAuthorAddress(address);
//                articleChain.setTransactionHash(transactionHash);
//                articleChain.setTitle(title);
//                articleChain.setTimestamp(publicationDate);
//                articleChainService.save(articleChain);
//            }
//
//            return R.success("成功發布文章");
//        } catch (Exception e) {
//            log.error("Failed to upload article", e);
//            return R.error("Failed to upload article");
//        }
//    }


    /**
     * 文章留言列表
     *
     * @return R
     */
//    @RequestMapping("/commentList")
//    public R getCommentList() {
//        log.debug("debug:getCommentList");
//         List<Comment> commentList = commentService.list();
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//
//        for(Comment comment : commentList) {
//            Integer id = comment.getUser_id();
//            userService.getOne(wrapper.eq(User::getId, id));
//            User user = userService.getOne(wrapper.eq(User::getId, id));
//            comment.setAuthor(user.getName());
//        }
//
//
//        log.debug("debug:getCommentList" + commentList.toString());
//        return R.success(commentList);
//    }


    /**
     * 文章留言列表
     *
     * @param articleId
     * @return
     */
//    @GetMapping("/commentList/{articleId}")
//    public R getCommentsByArticleId(@PathVariable Integer articleId) {
//        log.debug("debug:getCommentList");
//        List<Comment> commentList = commentService.getCommentsByArticleId(articleId);
//        log.debug(commentList.toString());
//        return R.success(commentList);
//    }
    @GetMapping("/commentList/{articleId}")
    public R getCommentsByArticleId(@PathVariable Integer articleId, @RequestParam Integer userId) {
        List<CommentDto> commentList = commentService.getCommentsByArticleId(articleId, userId);
        return R.success(commentList);
    }

    /**
     * 新增留言
     *
     * @return R
     */
    @PostMapping("/addComment")
    public R addComment(@RequestBody Comment comment) {
//        log.debug("debug:addComment" + comment.toString());
        commentService.addComment(comment);
        articleService.updateCommentCount(comment.getArticleId());
        return R.success("新增留言成功");
    }

    @PostMapping("/likeComment/{commentId}")
    public R likeComment(@RequestParam Integer userId, @PathVariable Integer commentId) {
        log.debug("debug:likeComment" + commentId.toString());
        boolean success = commentService.likeComment(userId, commentId);
        if (success) {
            return R.success("按讚成功");
        } else {
            return R.error("您已經按過讚");
        }
    }

    @PostMapping("/addBookmark")
    public R addBookmark(@RequestParam Integer userId, @RequestParam Integer articleId) {
        boolean success = favoriteService.addFavorite(userId, articleId);

        if (success) {
            articleService.updateFavoriteCount(articleId, 1);
            return R.success("收藏成功");
        } else return R.error("蒐藏失敗");
    }

    @PostMapping("/removeBookmark")
    public R removeBookmark(@RequestParam Integer userId, @RequestParam Integer articleId) {
        boolean success = favoriteService.removeFavorite(userId, articleId);
        if (success) {
            articleService.updateFavoriteCount(articleId, -1);
            return R.success("取消收藏成功");
        } else return R.error("取消收藏失敗");
    }

    @PostMapping("/addLove")
    public R addLove(@RequestParam Integer userId, @RequestParam Integer articleId) {
        boolean success = loveService.addLove(userId, articleId);

        if (success) {
            articleService.updateLoveCount(articleId, 1);
            return R.success("按讚成功");
        } else return R.error("按讚失敗");
    }

    @PostMapping("/removeLove")
    public R removeLove(@RequestParam Integer userId, @RequestParam Integer articleId) {
        boolean success = loveService.removeLove(userId, articleId);
        if (success) {
            articleService.updateLoveCount(articleId, -1);
            return R.success("取消按讚成功");
        } else return R.error("取消按讚失敗");
    }
}


