package com.shihHsin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.User;

/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-10
 */
public interface IUserService extends IService<User> {
    String getUserNameById(Integer id);
}
