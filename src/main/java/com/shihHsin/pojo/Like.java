package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("likes")
public class Like {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer commentId;
}