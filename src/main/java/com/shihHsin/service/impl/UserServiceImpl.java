package com.shihHsin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.UserMapper;
import com.shihHsin.pojo.User;
import com.shihHsin.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
