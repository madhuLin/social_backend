package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.Article2Dto;
import com.shihHsin.Dto.ArticleDto;
import com.shihHsin.Dto.CommentDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.*;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public IBoardService boardService;
    @Resource
    public IArticleChainService articleChainService;
//    @Resource
//    private IIpfsService ipfsService;

    @Resource
    private IUserService userService;

    @Resource
    public ICommentService commentService;

    @Resource
    public IBookmarkService favoriteService;

    @Resource
    public ILikeService likeService;

    @Resource
    public IImageService imageService;

    @Resource
    public IActivitieService activitieService;

    private CompletableFuture<List<String>> findArticleImagesAsync(int articleId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Image> images = imageService.findImagesByArticleId(articleId);
            List<String> imageBytes = new ArrayList<>();
            for (Image image : images) {
                Path path = Paths.get(uploadPath, image.getImagePath());
                try {
                    byte[] fileContent = Files.readAllBytes(path);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    imageBytes.add(encodedString);
                } catch (Exception e) {
                    log.error("Failed to read image file", e);
                }
            }
            return imageBytes;
        });
    }

    @GetMapping("/list")
    public R getArticleList(@RequestParam(value = "boardId", required = false) Integer boardId,
                            @RequestParam(value = "userId", required = false) Integer userId) {
        log.debug("debug:getArticleList" + (boardId != null ? boardId.toString() : "null") + (userId != null ? userId.toString() : "null"));
        List<Article> articleList;
        if (boardId != null) {
            articleList = articleService.getByBoardId(boardId);
        } else {
            articleList = articleService.list();
        }

        Map<Integer, String> boardName = boardService.getBoardNames();
        List<CompletableFuture<Article2Dto>> futureList = new ArrayList<>();

        for (Article article : articleList) {
            CompletableFuture<Article2Dto> future = CompletableFuture.supplyAsync(() -> {
                Article2Dto article2Dto = new Article2Dto(article);
                if (userId != null) {
                    boolean isBookmarked = favoriteService.isArticleBookmarkedByUser(article.getId(), userId);
                    boolean isLove = likeService.isArticleLikeByUser(article.getId(), userId);
                    boolean isDislike = likeService.isArticleDislikeByUser(article.getId(), userId);
                    article2Dto.setBookmarked(isBookmarked);
                    article2Dto.setLiked(isLove);
                    article2Dto.setDisliked(isDislike);
                }
                article2Dto.setBoardName(boardName.get(article.getBoardId()));
                return article2Dto;
            }).thenCombine(findArticleImagesAsync(article.getId()), (article2Dto, images) -> {
                article2Dto.setImages(images);
                return article2Dto;
            });

            futureList.add(future);
        }

        List<Article2Dto> article2DtoList = futureList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

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
//    public R loadIPFS(@RequestBody Map<String, Object> requestBody) {
//        String content = (String) requestBody.get("content");
//        log.debug("Received article content: {}", content);
//        String ipfsHash = null;
//        try {
//            byte[] data = content.getBytes();
//            ipfsHash = ipfsService.uploadToIpfs(data);
//        } catch (Exception e) {
//            log.error("Failed to upload article to IPFS", e);
//            return R.error("Failed to upload article to IPFS");
//        }
//        return R.success(ipfsHash);
//    }

    /**
     * 上傳
     *
     * @param article Data
     * @return R
     */
    @PostMapping("/upload")
    public R upload(@ModelAttribute ArticleDto article, HttpSession session) {
        log.debug("title:" + article.getTitle()+  "time:"+ article.getPublicationDate());
        try {
            // 創建並保存文章實體
            Article articleEntity = new Article();
            articleEntity.setAuthorId(article.getId());
            articleEntity.setTitle(article.getTitle());
            articleEntity.setContent(article.getContent());
            articleEntity.setAuthorName(article.getUsername());
            articleEntity.setAuthorAddress(article.getAddress());
            Timestamp timestamp = new Timestamp(article.getPublicationDate());
            articleEntity.setPublicationDate(timestamp);
            articleEntity.setChained(article.isChained());
            articleEntity.setState(true);

            // 先保存文章以獲取自動生成的ID
            articleService.save(articleEntity);
            Integer articleId = articleEntity.getId();

            // 保存圖片
            List<MultipartFile> images = article.getImages();
            if (images != null) {
                int imageOrder = 1;
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                        Path filePath = Paths.get(uploadPath, "article", fileName);
                        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                        Image img = new Image();
                        img.setArticleId(articleId);  // 使用正確的 articleId
                        img.setImagePath("article" + "/" + fileName);
                        img.setImageOrder(imageOrder++);
                        imageService.save(img);
                    }
                }
            }

            // 如果文章被鏈接，保存鏈接信息
            if (article.isChained()) {
                ArticleChain articleChain = new ArticleChain();
                articleChain.setArticleId(articleId);  // 使用正確的 articleId
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

    @PostMapping("/recordBlockchain")
    public R recordToBlockchain(@RequestBody ArticleChain article) {
        log.debug("debug:recordToBlockchain" + article.toString());
        try {
            articleService.setchained(article.getArticleId());
            boolean res = articleChainService.save(article);
            if(res) {
                return R.success("成功鏈接文章");
            } else {
                return R.error("鏈接文章失敗");
            }
        } catch (Exception e) {
            log.error("Failed to record article to blockchain", e);
            return R.error("Failed to record article to blockchain");
        }
    }

    /**
     * 文章留言列表
     *
     * @param articleId
     * @return
     */
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

    @PostMapping("/addLike")
    public R addLike(@RequestParam Integer userId, @RequestParam Integer articleId) {
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getId, articleId);
        Integer authorId = articleService.getOne(articleWrapper).getAuthorId();

        Activitie activitie = new Activitie();
        activitie.setUserId(userId);
        activitie.setTargetId(authorId);
        activitie.setType("like");
        activitieService.save(activitie);
        boolean success = likeService.addLike(userId, articleId);
        if (success) {
            articleService.updateLikeCount(articleId, 1);
            return R.success("按讚成功");
        } else return R.error("按讚失敗");
    }

    @PostMapping("/addDislike")
    public R addDislike(@RequestParam Integer userId, @RequestParam Integer articleId) {
        boolean success = likeService.addDislike(userId, articleId);

        if (success) {
            articleService.updateLikeCount(articleId, 0);
            return R.success("成功");
        } else return R.error("失敗");
    }

    @PostMapping("/removeLove")
    public R removeLove(@RequestParam Integer userId, @RequestParam Integer articleId) {
        boolean success = likeService.removeLike(userId, articleId);
        if (success) {
            articleService.updateLikeCount(articleId, -1);
            return R.success("取消按讚成功");
        } else return R.error("取消按讚失敗");
    }

    @RequestMapping("/search={words}")
    public R serachArticle(@PathVariable("words") String words){
        LambdaQueryWrapper<Article> searchTitleWrapper = new LambdaQueryWrapper();
        searchTitleWrapper.like(Article::getTitle, words);
        List<Article> articleTitleList = articleService.list(searchTitleWrapper);
        LambdaQueryWrapper<Article> searchContentWrapper = new LambdaQueryWrapper<>();
        searchContentWrapper.like(Article::getContent, words);
        List<Article> articleContentList = articleService.list(searchContentWrapper);

        List<Article> articleList = new ArrayList<>();
        articleList.addAll(articleTitleList);
        articleList.addAll(articleContentList);

        return R.success(articleList);
    }

    @GetMapping("/getArticleChainId")
    public R getArticleChainId(@RequestParam Integer articleId) {
        ArticleChain articleChain = articleChainService.getOne(new LambdaQueryWrapper<ArticleChain>()
                .eq(ArticleChain::getArticleId, articleId));
        if (articleChain == null) {
            return R.error("Article not found");
        }

        return R.success(articleChain.getId());
    }
}


