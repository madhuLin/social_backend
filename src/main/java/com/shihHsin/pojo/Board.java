package com.shihHsin.pojo;

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
    @TableId
    private Integer boardId;

    @TableField("board_name") // 指定與資料庫表中欄位的對應關係
    private String boardName;

    @TableField("board_description") // 指定與資料庫表中欄位的對應關係
    private String boardDescription;

    @TableField("admin_id") // 指定與資料庫表中欄位的對應關係
    private int adminId;

    @TableField("creation_time") // 指定與資料庫表中欄位的對應關係
    private Timestamp creationTime;

    @TableField("followers_count") // 指定與資料庫表格中欄位的對應關係
    private int followersCount;

    @TableField("board_logo") // 指定與資料庫表中欄位的對應關係
    private String boardLogo;

    @TableField("board_status") // 指定與資料庫表中欄位的對應關係
    private int boardStatus;
}
