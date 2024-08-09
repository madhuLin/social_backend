package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer authorId;
    private Integer boardId;
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
}

