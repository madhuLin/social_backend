package com.shihHsin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.UserMapper;
import com.shihHsin.pojo.User;
import com.shihHsin.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String getUserNameById(Integer id) {
        User user = baseMapper.selectById(id);
        // 檢查用戶是否存在
        if (user == null) {
            return null; // 或者拋出一個自定義異常
        }
        // 返回用戶的名稱
        return user.getName();
    }

    @Override
    public String getAvatarByUserId(Integer userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        User user = baseMapper.selectOne(queryWrapper);

        // 打印日志信息
//        log.debug("User avatar: " + user.getAvatar() + "  userId: " + userId);

        // 直接检查 avatar 字段
        if (user == null || user.getAvatar() == null || user.getAvatar().isEmpty()) {
//            log.debug("Avatar is missing for userId: {}", userId);
            return getDefaultAvatar(); // 返回默认头像的Base64编码
        }

        Path path = Paths.get(uploadPath, user.getAvatar());

        try {
            byte[] fileContent = Files.readAllBytes(path);
            // 返回用户的头像
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error("Failed to read image file at path: " + path, e);
            return getDefaultAvatar(); // 如果读取失败，返回默认头像的Base64编码
        }
    }

    private String getDefaultAvatar() {
        try {
            Path defaultAvatarPath = Paths.get(uploadPath, "user/default-avatar.jpg"); // 设定默认头像的路径
            byte[] fileContent = Files.readAllBytes(defaultAvatarPath);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error("Failed to read default avatar image file.", e);
            return ""; // 或者返回一个占位符，表示无法获取头像
        }
    }

    @Override
    public String getUserAddressByUserId(Integer userId) {
        User user = baseMapper.selectById(userId);
        return user.getAddress();
    }

}
