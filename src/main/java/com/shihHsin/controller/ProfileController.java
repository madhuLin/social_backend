package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shihHsin.Dto.*;
import com.shihHsin.common.R;
import com.shihHsin.pojo.*;
import com.shihHsin.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Resource
    public IFollowService followService;
    @Resource
    public IArticleService articleService;
    @Resource
    public IUserService userService;
    @Resource
    public IFollowBoardService followBoardService;
    @Resource
    public IBoardService boardService;
    @Resource
    public IBookmarkService bookmarkService;

    @RequestMapping("/list")
    public R getProfileList(@RequestParam(value = "id") Integer id, @RequestParam(value = "userId", required = false) Integer userId){
        LambdaQueryWrapper<User> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(User::getId, Long.valueOf(id));
        String name = userService.getOne(nameWrapper).getName();
        String intro = userService.getOne(nameWrapper).getIntro();

        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getAuthorId, id);
        long articleCount = articleService.count(articleWrapper);

        LambdaQueryWrapper<Follow> followerWrapper = new LambdaQueryWrapper<>();
        followerWrapper.eq(Follow::getFollowerId, id);
        long followCount = followService.count(followerWrapper);

        LambdaQueryWrapper<Follow> followedWrapper = new LambdaQueryWrapper<>();
        followedWrapper.eq(Follow::getFolloweeId, id);
        long followedCount = followService.count(followedWrapper);
        log.debug(String.valueOf(followedCount));

        FollowDto followDto = new FollowDto();
        followDto.setName(String.valueOf(name));
        followDto.setArticleCount(articleCount);
        followDto.setFollowerCount(followCount);
        followDto.setFollowedCount(followedCount);
        followDto.setIntro(intro);
        followDto.setAvatar(userService.getAvatarByUserId(id));
        followDto.setMyFollowing(followService.getMyFollowed(id, userId));

        return R.success(followDto);
    }

    @RequestMapping("/myarticle={id}")
    public R getMyArticleList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getAuthorId, id);
        List<Article> articleList = articleService.list(articleWrapper);
        List<ArticleSummaryDto> articleSummaryDtos = new ArrayList<>();
        for( Article article : articleList) {
            ArticleSummaryDto articleSummaryDto = new ArticleSummaryDto(article);
            articleSummaryDto.setUsername(userService.getUserNameById(article.getAuthorId()));
//            articleSummaryDto.setAvatar(userService.getAvatarByUserId(article.getAuthorId()));
            articleSummaryDtos.add(articleSummaryDto);
        }
        return R.success(articleSummaryDtos);
    }

    @GetMapping("/mybookmark/{id}")
    public R getMyBookMarkList(@PathVariable("id") Integer id) {
        // 先查詢書籤列表中的文章 ID
        List<Integer> articleIds = bookmarkService.list(new LambdaQueryWrapper<Bookmark>()
                        .eq(Bookmark::getUserId, id))
                .stream()
                .map(Bookmark::getArticleId)
                .collect(Collectors.toList());

        // 如果書籤列表不為空，根據文章 ID 查詢對應的文章列表
        List<Article> articleList = articleIds.isEmpty() ? new ArrayList<>() :
                articleService.list(new LambdaQueryWrapper<Article>()
                        .in(Article::getId, articleIds));
        List<ArticleBookMarkDto> articleBookMarkDtos = new ArrayList<>();
        for (Article article : articleList) {
            ArticleBookMarkDto articleBookMarkDto = new ArticleBookMarkDto(article);
            articleBookMarkDto.setAuthorName(userService.getUserNameById(article.getAuthorId()));
            articleBookMarkDto.setAvatar(userService.getAvatarByUserId(article.getAuthorId()));
            articleBookMarkDtos.add(articleBookMarkDto);
        }
        return R.success(articleBookMarkDtos);
    }

    @RequestMapping("/edit={id}")
    public R getEditList(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<User> editWrapper = new LambdaQueryWrapper<>();
        editWrapper.eq(User::getId, id);

        // 使用 getOne 查詢單一 User
        User user = userService.getOne(editWrapper);

        // 若 user 為 null，則返回錯誤信息或處理
        if (user == null) {
            return R.error("User not found");
        }

        // 查詢 avatar
        String avatar = userService.getAvatarByUserId(id);

        // 創建 UserDto 並設置屬性
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddress());
        userDto.setEmail(user.getEmail());
        userDto.setSex(user.getSex());
        userDto.setAvatar(avatar);
        userDto.setIntro(user.getIntro());

        return R.success(userDto);
    }

    @RequestMapping("/myfollowing={id}")
    public R getMyFollowList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<Follow> followerWrapper = new LambdaQueryWrapper<>();
        followerWrapper.eq(Follow::getFollowerId, id);
        List<Follow> followList = followService.list(followerWrapper);

        List<Integer> followeeIds = followList.stream()
                .map(Follow::getFolloweeId)
                .collect(Collectors.toList());

        List<FollowDto> followDto = new ArrayList<>();
        for (Integer followeeId : followeeIds) {
            LambdaQueryWrapper<Article> articleCountWrapper = new LambdaQueryWrapper<>();
            articleCountWrapper.eq(Article::getAuthorId, followeeId);
            long articleCount = articleService.count(articleCountWrapper);

            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getId, followeeId);
            String userName = userService.getOne(userWrapper).getName();

            LambdaQueryWrapper<Follow> followCountWrapper = new LambdaQueryWrapper<>();
            followCountWrapper.eq(Follow::getFollowerId, followeeId);
            long followCount = followService.count(followCountWrapper);

            LambdaQueryWrapper<Follow> followedCountWrapper = new LambdaQueryWrapper<>();
            followedCountWrapper.eq(Follow::getFolloweeId, followeeId);
            long followedCount = followService.count(followedCountWrapper);

            FollowDto followDto1 = new FollowDto();
            followDto1.setId(followeeId);
            followDto1.setName(String.valueOf(userName));
            followDto1.setArticleCount(articleCount);
            followDto1.setFollowerCount(followCount);
            followDto1.setFollowedCount(followedCount);
            followDto1.setAvatar(userService.getAvatarByUserId(followeeId));
            followDto.add(followDto1);
        }

        return R.success(followDto);
    }

    @RequestMapping("/myfollowed={id}")
    public R getMyFollowedList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<Follow> followedWrapper = new LambdaQueryWrapper<>();
        followedWrapper.eq(Follow::getFolloweeId, id);
        List<Follow> followedList = followService.list(followedWrapper);

        List<Integer> followerIds = followedList.stream()
                .map(Follow::getFollowerId)
                .collect(Collectors.toList());

        List<FollowDto> followDto = new ArrayList<>();
        for (Integer followerId : followerIds) {
            LambdaQueryWrapper<Article> articleCountWrapper = new LambdaQueryWrapper<>();
            articleCountWrapper.eq(Article::getAuthorId, followerId);
            long articleCount = articleService.count(articleCountWrapper);

            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getId, followerId);
            String userName = userService.getOne(userWrapper).getName();

            LambdaQueryWrapper<Follow> followCountWrapper = new LambdaQueryWrapper<>();
            followCountWrapper.eq(Follow::getFollowerId, followerId);
            long followCount = followService.count(followCountWrapper);

            LambdaQueryWrapper<Follow> followedCountWrapper = new LambdaQueryWrapper<>();
            followedCountWrapper.eq(Follow::getFolloweeId, followerId);
            long followedCount = followService.count(followedCountWrapper);

            FollowDto followDto1 = new FollowDto();
            followDto1.setId(followerId);
            followDto1.setName(String.valueOf(userName));
            followDto1.setArticleCount(articleCount);
            followDto1.setFollowerCount(followCount);
            followDto1.setFollowedCount(followedCount);
            followDto1.setMyFollowing(followService.getMyFollowed(followerId, id));
            followDto1.setAvatar(userService.getAvatarByUserId(followerId));
            followDto.add(followDto1);
        }
        log.debug(followDto.toString());
        return R.success(followDto);
    }

    @RequestMapping("/myfollowboard={id}")
    public R getMyBoardList(@PathVariable("id") Integer id) {
        // 查詢追蹤的看板ID
        LambdaQueryWrapper<FollowBoard> fbWrapper = new LambdaQueryWrapper<>();
        fbWrapper.eq(FollowBoard::getUserId, id);
        List<Integer> followboardIds = followBoardService.list(fbWrapper).stream()
                .map(FollowBoard::getBoardId)
                .collect(Collectors.toList());

        // 批量查詢Board信息，避免重複查詢
        if (followboardIds.isEmpty()) {
            return R.success(Collections.emptyList());
        }
        LambdaQueryWrapper<Board> boardWrapper = new LambdaQueryWrapper<>();
        boardWrapper.in(Board::getId, followboardIds);
        List<Board> boards = boardService.list(boardWrapper);

        // 將查詢結果轉換為BoardDto列表
        List<BoardDto> boardDtoList = boards.stream().map(board -> {
            BoardDto boardDto = new BoardDto();
            boardDto.setName(board.getName());
            boardDto.setDescription(board.getDescription());
            boardDto.setLogoBase64(board.getLogo());
            return boardDto;
        }).collect(Collectors.toList());

        log.debug(boardDtoList.toString());
        return R.success(boardDtoList);
    }


    @Value("${upload.path}")
    private String uploadPath;


    //    編輯個人資料
    @PutMapping("/put")
    public R<String> updateUser (@RequestBody User user) throws IOException {
        log.debug("update user: {}", user.toString());
        userService.updateById(user);
        return R.success(null);
    }

    //    編輯個人資料圖片
    @PutMapping("/upload")
    public R<String> upload(@ModelAttribute UserAvatarDto userAvatarDto) throws IOException {
        MultipartFile file = userAvatarDto.getAvatar();
        Integer id = userAvatarDto.getId();
        log.debug("file name: {}", file.getOriginalFilename());

        // 創建一個 UUID 來命名圖片
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 將圖片保存到本地
        Path path = Paths.get(uploadPath, "user", fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 刪除舊的圖片
        String oldAvatar = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, id)).getAvatar();
        if (oldAvatar != null) {
            Path oldPath = Paths.get(uploadPath + oldAvatar);
            if (Files.exists(oldPath)) {  // 檢查檔案是否存在
                try {
                    FileUtils.forceDelete(oldPath.toFile());  // 檔案存在，則刪除
                } catch (IOException e) {
                    // 如果刪除時出現錯誤，進行錯誤處理
                    log.error("刪除檔案失敗: " + e.getMessage());
                }
            } else {
                // 檔案不存在的情況下，可以選擇進行其他操作或僅忽略
                log.error("檔案不存在，無需刪除");
            }
        }

        // 使用 UpdateWrapper 只更新 avatar 欄位
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id).set(User::getAvatar, "user/" + fileName);

        userService.update(updateWrapper);

        return R.success("成功上傳圖片!");
    }

    //    追蹤
    @PostMapping("/track")
    public R<String> track(@RequestParam("id") Integer id, @RequestParam("userId") Integer userId) {
        followService.followUser(id, userId);
        return R.success("成功關注!");
    }

    @PostMapping("/unTrack")
    public R<String> unTrack(@RequestParam("id") Integer id, @RequestParam("userId") Integer userId) {
        followService.unfollowUser(id, userId);
        return R.success("成功取消關注!");
    }
}