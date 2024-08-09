package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Activitie {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String type;
    private int userId;
    private int targetId;
    private Timestamp time;
}
