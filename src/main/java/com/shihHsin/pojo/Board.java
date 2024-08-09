package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class Board {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private int adminId;
    private Timestamp creationTime;
    private int followersCount;
    private String logo;
    private int status;
}
