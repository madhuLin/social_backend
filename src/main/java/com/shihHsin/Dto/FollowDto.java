package com.shihHsin.Dto;

import lombok.Data;

@Data
public class FollowDto {
    private Integer id;
    private String name;
    private String intro;
    private long followerId;
    private long followeeId;
    private long articleCount;
    private long followerCount;
    private long followedCount;
}
