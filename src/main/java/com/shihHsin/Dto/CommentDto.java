package com.shihHsin.Dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDto {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private String content;
    private Timestamp commentDate;
    private Integer likeCount;
    private String authorName;
    private Boolean liked;
}
