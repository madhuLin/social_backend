package com.shihHsin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shihHsin.Dto.LoginDto;
import com.shihHsin.Dto.SignUpDto;
import com.shihHsin.common.R;
import com.shihHsin.pojo.User;
import com.shihHsin.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
@CrossOrigin(origins = "http://localhost:5173") //dev
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 註冊
     * @param signUpDto Data
     * @return R
     */
    @PostMapping("/signUp")
    public R signUp(@RequestBody SignUpDto signUpDto, HttpSession session) {
        // 檢查使用者名稱是否已經存在
//        log.debug("run signUp" + signUpDto.toString());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User existingUser = userService.getOne(wrapper.eq(User::getUsername, signUpDto.getUsername()));
        if (existingUser != null) {
            return R.error("使用者名稱已存在!");
        }

        // 建立使用者物件並設定屬性
        User newUser = new User();
        newUser.setUsername(signUpDto.getUsername());
        newUser.setAddress(signUpDto.getAddress());
        newUser.setEmail(signUpDto.getEmail());
        newUser.setPassword(signUpDto.getPassword()); // 在實際應用程式中，請務必對密碼進行安全處理，如雜湊處理

        // 其他屬性設定（性別、註冊日期等）
//        newUser.setSex(signUpDto.getSex());
        newUser.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now())); // 或從前端取得註冊日期

        // 執行註冊
        boolean saved = userService.save(newUser);
        if (saved) {
            // 註冊成功
            return R.success("註冊成功");
        } else {
            // 註冊失敗
            return R.error("註冊失敗");
        }
    }


    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto, HttpSession session) {
        // 檢查使用者名稱是否已經存在
        log.debug("run loginDto" + loginDto.toString());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User user = userService.getOne(wrapper.eq(User::getUsername, loginDto.getUsername()));
        if (user == null) {
            return R.error("帳號不存在!");
        }
        else {
            log.debug("password" + user.getPassword() + loginDto.getPassword());
            if(user.getPassword().equals(loginDto.getPassword())) return R.success("成功登入!!");
            else return R.error("密碼錯誤!");
        }
    }


//    @PostMapping("/login")
//    public R login(@RequestBody SignUpDto loginDto, HttpSession session){
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        User user = userService.getOne(wrapper.eq(User::getPhone, loginDto.getPhone()));
//        String  is = (String)myRedisTemplate.opsForValue().get("code");
//        if (loginDto.getCode().equals(is)){
//            User curUser = new User();
//            if (user == null){
//                curUser.setPhone(loginDto.getPhone());
//                curUser.setStatus(1);
//                userService.save(curUser);
//            }else {
//                curUser = user;
//            }
//            session.setAttribute("user", curUser);
//            return R.success(user);
//        }
//        return R.error("登录失败");
//    }
}

