package com.shihHsin.Dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
public class BoardDto {
    @TableId
    private Integer id;
    private String name;
    private String description;
    private Timestamp creationTime;
    private int followersCount;
    private String logoBase64;
}
