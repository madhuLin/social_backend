package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.pojo.User;
import com.shihHsin.mapper.TestMapper;
import com.shihHsin.service.ITestService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, User> implements ITestService {
    @Resource
    private TestMapper testMapper;

    @Override
    public List<User> getUsers() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(User::getUserId);
//        List<User> users = list(wrapper);
        // 調用 service 或 mapper 的相應方法執行查詢，這裡假設您已經注入了相應的 service 或 mapper
//        return users;

        return testMapper.selectList(wrapper);
    }
}
