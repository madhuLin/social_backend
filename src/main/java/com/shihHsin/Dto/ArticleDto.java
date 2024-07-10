package com.shihHsin.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class ArticleDto {
    private String address;
    private String username;
    private String title;
    private String content;
    private List<MultipartFile> images;
    private Long publicationDate; // 文章创建时间
    private boolean chained; // 是否上鏈
    private String transactionHash;
}

