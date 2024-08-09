package com.shihHsin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.LoginDto;
import com.shihHsin.Dto.SignUpDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.Follow;
import com.shihHsin.pojo.User;
import com.shihHsin.service.IFollowService;
import com.shihHsin.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@RestController
@Slf4j
//@CrossOrigin(origins = "http://localhost:5173") //dev
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IFollowService followService;

    @GetMapping("/checkUsername/{username}")
    public R checkUsername(@PathVariable String username) {
        log.debug("run checkUsername:" + username);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User existingUser = userService.getOne(wrapper.eq(User::getName, username));
        if (existingUser != null) {
            return R.error("使用者名稱已存在!");
        } else {
            return R.success("使用者名稱可用");
        }
    }


    /**
     * 註冊
     * @param signUpDto Data
     * @return R
     */
    @PostMapping("/signUp")
    public R signUp(@RequestBody SignUpDto signUpDto, HttpSession session) {
        // 檢查使用者名稱是否已經存在
        log.debug("run signUp" + signUpDto.toString());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User existingUser = userService.getOne(wrapper.eq(User::getName, signUpDto.getUsername()));
        if (existingUser != null) {
            return R.error("使用者名稱已存在!");
        }

        log.debug("run signUpAA:" + signUpDto.toString());
        // 建立使用者物件並設定屬性
        User newUser = new User();
        newUser.setName(signUpDto.getUsername());
        newUser.setAddress(signUpDto.getAddress());
        newUser.setEmail(signUpDto.getEmail());
        String password = signUpDto.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        newUser.setPassword(password); // 在實際應用程式中，請務必對密碼進行安全處理，如雜湊處理

        // 其他屬性設定（性別、註冊日期等）
        newUser.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now())); // 或從前端取得註冊日期
        newUser.setStatus(true); // 註冊成功後，預設啟用帳號
        // 執行註冊
        boolean saved = userService.save(newUser);
        if (!saved) {
            // 註冊失敗
            return R.error("註冊失敗");
        }
        return R.success("註冊成功");
    }


    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto, HttpSession session) {
        // 檢查使用者名稱是否已經存在
        log.debug("run loginDto" + loginDto.toString());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User user = userService.getOne(wrapper.eq(User::getName, loginDto.getUsername()));
        if (user == null) {
            return R.error("帳號不存在!");
        }
        log.debug("password" + user.getPassword() + loginDto.getPassword());
        String password = loginDto.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return R.error("密碼錯誤!");

        }
        if(!user.isStatus()) return R.error("帳號已被禁用!");
        session.setAttribute("user", user);
        return R.success(user);
    }

    @GetMapping("/logout")
    public R logout(HttpSession session) {
        session.invalidate();
        return R.success("登出成功");
    }



    @PostMapping("/follow")
    public R followUser(@RequestParam Integer followerId, @RequestParam Integer followeeId) {
        try {
            followService.followUser(followerId, followeeId);
            return R.success("成功追蹤使用者");
        } catch (Exception e) {
            return R.error("追蹤使用者失敗");
        }
    }

    @PostMapping("/unfollow")
    public R unfollowUser(@RequestParam Integer followerId, @RequestParam Integer followeeId) {
        try {
            followService.unfollowUser(followerId, followeeId);
            return R.success("成功取消追蹤使用者");
        } catch (Exception e) {
            return R.error("取消追蹤使用者失敗");
        }
    }

    @GetMapping("/followers")
    public R getFollowers(@RequestParam Integer followeeId) {
        List<Follow> followers = followService.getFollowers(followeeId);
        return R.success(followers);
    }

    @GetMapping("/following")
    public R getFollowing(@RequestParam Integer followerId) {
        List<Follow> following = followService.getFollowing(followerId);
        return R.success(following);
    }




}

