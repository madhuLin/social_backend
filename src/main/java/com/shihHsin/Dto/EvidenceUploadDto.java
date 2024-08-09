package com.shihHsin.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Data
public class EvidenceUploadDto {
    private int id;                    // 自動遞增的主鍵
    private List<MultipartFile> images;
    private int verificationId;        // 驗證 ID
    private String description;        // 描述
    private int userId;                // 提供者 ID
    private String transactionHash;    // 交易哈希
    private Timestamp evidenceDate; // 證據日期
}
