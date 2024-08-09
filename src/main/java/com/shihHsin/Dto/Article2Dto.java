package com.shihHsin.Dto;

import com.shihHsin.pojo.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article2Dto {
    private Integer id;
    private String title;
    private String content;
    private String boardName;
    private Integer authorId;
    private String authorName;
    private String authorAddress;
    private Timestamp publicationDate;
    private boolean state;
    private Integer readCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer bookmarkCount;
    private boolean chained;
    private boolean bookmarked; // 是否收藏
    private boolean liked; // 是否喜欢
    private boolean disliked; // 是否不喜欢
    private List<String> images;
    public Article2Dto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.authorId = article.getAuthorId();
        this.authorName = article.getAuthorName();
        this.authorAddress = article.getAuthorAddress();
        this.publicationDate = article.getPublicationDate();
        this.state = article.isState();
        this.readCount = article.getReadCount();
        this.likeCount = article.getLikeCount();
        this.dislikeCount = article.getDislikeCount();
        this.commentCount = article.getCommentCount();
        this.bookmarkCount = article.getBookmarkCount();
        this.chained = article.isChained();
    }
}
