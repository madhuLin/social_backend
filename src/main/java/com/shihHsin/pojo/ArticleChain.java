package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class ArticleChain {
    private Integer id;
    private Integer articleId;
    private String transactionHash;
    private String authorAddress;
    private String title;
    private Long timestamp;
}
