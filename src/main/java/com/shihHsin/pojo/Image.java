package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Image {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer articleId;
    private String imagePath;
    private Integer imageOrder;
}
