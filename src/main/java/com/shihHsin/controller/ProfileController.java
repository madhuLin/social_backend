package com.shihHsin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.BoardDto;
import com.shihHsin.Dto.FollowDto;
import com.shihHsin.Dto.UserDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.*;
import com.shihHsin.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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


    @RequestMapping("/{id}")
    public R getProfileList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<User> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(User::getId, Long.valueOf(id));
        String name = userService.getOne(nameWrapper).getName();
        String intro = userService.getOne(nameWrapper).getIntro();

        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getAuthorId, id);
        long artcileCount = articleService.count(articleWrapper);

        LambdaQueryWrapper<Follow> followerWrapper = new LambdaQueryWrapper<>();
        followerWrapper.eq(Follow::getFollowerId, id);
        long followCount = followService.count(followerWrapper);

        LambdaQueryWrapper<Follow> followedWrapper = new LambdaQueryWrapper<>();
        followedWrapper.eq(Follow::getFolloweeId, id);
        long followedCount = followService.count(followedWrapper);
        log.debug(String.valueOf(followedCount));

        FollowDto followDto = new FollowDto();
        followDto.setName(String.valueOf(name));
        followDto.setArticleCount(artcileCount);
        followDto.setFollowerCount(followCount);
        followDto.setFollowedCount(followedCount);
        followDto.setIntro(intro);

        return R.success(followDto);
    }

    @RequestMapping("/myarticle={id}")
    public R getMyArticleList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getAuthorId, id);
        List<Article> articleList = articleService.list(articleWrapper);
        return R.success(articleList);
    }

    @RequestMapping("/mybookmark={id}")
    public R getMyBookMarkList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<Bookmark> bookmarkWrapper = new LambdaQueryWrapper<>();
        bookmarkWrapper.eq(Bookmark::getUserId, id);
        List<Bookmark> bookmarkList = bookmarkService.list(bookmarkWrapper);
        List<Integer> bmListIds = bookmarkList.stream()
                .map(Bookmark::getArticleId)
                .collect(Collectors.toList());
//        log.debug(String.valueOf(bmListIds));

        List<Article> articleList1 = new ArrayList<>();
        for (Integer bmListId : bmListIds){
//            log.debug(String.valueOf(bmListId));
            LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
            articleWrapper.eq(Article::getId, bmListId);
            List<Article> articleList = articleService.list(articleWrapper);
            articleList1.addAll(articleList);
        }
        return R.success(articleList1);
    }

    @RequestMapping("/edit={id}")
    public R getEditList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<User> editWrapper = new LambdaQueryWrapper<>();
        editWrapper.eq(User::getId, id);
        List<User> userList = userService.list(editWrapper);

        String name = userService.getOne(editWrapper).getName();
        String address = userService.getOne(editWrapper).getAddress();
        String email = userService.getOne(editWrapper).getEmail();
        String sex = userService.getOne(editWrapper).getSex();
        String avatar = userService.getOne(editWrapper).getAvatar();
        String intro = userService.getOne(editWrapper).getIntro();

        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setAddress(address);
        userDto.setEmail(email);
        userDto.setSex(sex);
        userDto.setAvatar(avatar);
        userDto.setIntro(intro);
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

        // 进一步处理 followeeId，例如查询用户信息
//        List<User> followeeUsers = userService.listByIds(followeeIds);
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

            followDto.add(followDto1);
        }
        log.debug(followDto.toString());
        return R.success(followDto);
    }

    @RequestMapping("/myfollowboard={id}")
    public R getMyBoardList(@PathVariable("id") Integer id){
        LambdaQueryWrapper<FollowBoard> fbWrapper = new LambdaQueryWrapper<>();
        fbWrapper.eq(FollowBoard::getUserId, id);
        List<FollowBoard> fbList = followBoardService.list(fbWrapper);
        List<Integer> followboardIds = fbList.stream()
                .map(FollowBoard::getBoardId)
                .collect(Collectors.toList());

        List<BoardDto> boardDto = new ArrayList<>();
        for (Integer followboardId : followboardIds) {
            BoardDto boardDto1 = new BoardDto();
            LambdaQueryWrapper<Board> nameWrapper = new LambdaQueryWrapper<>();
            nameWrapper.eq(Board::getId, followboardId);
            String name = boardService.getOne(nameWrapper).getName();

            LambdaQueryWrapper<Board> discriptionWrapper = new LambdaQueryWrapper<>();
            discriptionWrapper.eq(Board::getId, followboardId);
            String discription = boardService.getOne(discriptionWrapper).getDescription();

            LambdaQueryWrapper<Board> logoBase64Wrapper = new LambdaQueryWrapper<>();
            logoBase64Wrapper.eq(Board::getId, followboardId);
            String logoBase64 = boardService.getOne(logoBase64Wrapper).getLogo();

            boardDto1.setName(name);
            boardDto1.setDescription(discription);
            boardDto1.setLogoBase64(logoBase64);

            boardDto.add(boardDto1);
        }
        log.debug(boardDto.toString());
        return R.success(boardDto);
    }

    //    編輯個人資料
    @PutMapping("/put")
    public R<User> updateUser (@RequestBody User user){
        log.info(user.toString());
        userService.updateById(user);
        return R.success(null);
    }
}