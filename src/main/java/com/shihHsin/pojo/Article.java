package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Data
public class Article {
    @TableId
    private Integer articleId;
    private String articleTitle;
    private String articleContent;
    private int articleAuthorId;
    private String articleAuthorName;
    private Timestamp publicationDate;
    private int articleState;
    private int readCount;
    private int likeCount;
    private int commentCount;
    private int bookmarkCount;
}

