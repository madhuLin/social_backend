package com.shihHsin.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Image {
    private Integer id;
    private Integer articleId;
    private String imagePath;
    private Integer imageOrder;
    private Timestamp createdAt;
}
