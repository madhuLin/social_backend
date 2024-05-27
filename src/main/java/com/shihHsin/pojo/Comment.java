package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int articleId;
    private int user_id;
    private String content;
    private Timestamp commentDate;
    private int likeCount;
    private String author;
}
