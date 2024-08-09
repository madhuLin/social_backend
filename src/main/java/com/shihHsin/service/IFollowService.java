package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Follow;

import java.util.List;

public interface IFollowService extends IService<Follow> {

    public void followUser(Integer followerId, Integer followeeId);
    public void unfollowUser(Integer followerId, Integer followeeId);
    public List<Follow> getFollowers(Integer followeeId);

    public List<Follow> getFollowing(Integer followerId);
}
