package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Follow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FollowMapper extends BaseMapper<Follow> {

    @Delete("DELETE FROM follows WHERE follower_id = #{followerId} AND followee_id = #{followeeId}")
    void unfollow(Integer followerId, Integer followeeId);
    @Select("SELECT * FROM follows WHERE follower_id = #{followerId}")
    List<Follow> findByFollowerId(Integer followerId);

    @Select("SELECT * FROM follows WHERE followee_id = #{followeeId}")
    List<Follow> findByFolloweeId(Integer followeeId);
}
