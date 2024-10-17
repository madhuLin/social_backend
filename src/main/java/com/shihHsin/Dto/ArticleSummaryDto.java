package com.shihHsin.Dto;

import com.shihHsin.pojo.Article;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArticleSummaryDto {
    private Integer id;
    private String title;
    private String content;
    private String avatar;
    private String  username;
    private Integer authorId;
    private Integer boardId;
    private Timestamp publicationDate;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer bookmarkCount;

    public ArticleSummaryDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.authorId = article.getAuthorId();
        this.boardId = article.getBoardId();
        this.publicationDate = article.getPublicationDate();
        this.likeCount = article.getLikeCount();
        this.dislikeCount = article.getDislikeCount();
        this.commentCount = article.getCommentCount();
        this.bookmarkCount = article.getBookmarkCount();
    }
}
