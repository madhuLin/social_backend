package com.shihHsin.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Verification {
    private int id;
    private int articleId;
    private int userId;
    private int articleChainId;
    private String reason;
    private String transactionHash;
    private Timestamp verificationDate;
}
