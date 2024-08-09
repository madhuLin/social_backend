package com.shihHsin.Dto;

import com.shihHsin.pojo.Evidence;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Data
public class EvidenceDto {
    private int id;
    private int userId;
//    private int verificationId;        // 驗證 ID
    private String description;        // 描述
    private String username;                // 提供者 ID
    private Timestamp evidenceDate; // 證據日期
    private String transactionHash;    // 交易哈希
    private List<String> images;
    private boolean supported;
    private Integer supportCount;
    public EvidenceDto(Evidence evidence) {
        this.id = evidence.getId();
        this.userId = evidence.getUserId();
//        this.verificationId = evidence.getVerificationId();
        this.description = evidence.getDescription();
        this.transactionHash = evidence.getTransactionHash();
        this.evidenceDate = evidence.getEvidenceDate();
    }
}
