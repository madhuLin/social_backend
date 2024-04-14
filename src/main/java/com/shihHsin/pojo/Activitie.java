package com.shihHsin.pojo;

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
    @TableId
    private Integer activityId;
    private String activityType;
    private int userId;
    private int targetId;
    private Timestamp activityTime;
}
