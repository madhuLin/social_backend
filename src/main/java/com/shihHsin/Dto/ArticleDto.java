package com.shihHsin.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class ArticleDto {
    private String address;
    private String username;
    private String title;
    private String content;
    private byte[] coverPhoto; // 封面图片以字节数组形式存储
    private Long publicationDate; // 文章创建时间
    private boolean chained; // 是否上鏈
    private String transactionHash;
}

