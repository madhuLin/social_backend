package com.shihHsin.Dto;

import com.shihHsin.pojo.Article;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ArticleBookMarkDto {
    private Integer id;
    private String title;
    private String content;
    private String boardName;
    private Integer authorId;
    private String authorName;
    private Timestamp publicationDate;
    private Integer readCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private boolean bookmarked; // 是否收藏
    private boolean liked; // 是否喜欢
    private boolean disliked; // 是否不喜欢
    private List<String> images;
    private String avatar;
    public ArticleBookMarkDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.authorId = article.getAuthorId();
        this.publicationDate = article.getPublicationDate();
        this.readCount = article.getReadCount();
        this.likeCount = article.getLikeCount();
        this.dislikeCount = article.getDislikeCount();
        this.commentCount = article.getCommentCount();
    }
}
