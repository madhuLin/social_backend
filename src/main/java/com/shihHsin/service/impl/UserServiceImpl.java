package com.shihHsin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.UserMapper;
import com.shihHsin.pojo.User;
import com.shihHsin.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}
