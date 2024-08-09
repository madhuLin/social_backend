package com.shihHsin.Dto;

import com.shihHsin.pojo.Verification;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class VerificationDto {
    private int id;
    private int userId;
    private int articleId;
    private String userName;
    private String title;
    private String reason;
    private Timestamp verificationDate;
    public VerificationDto(Verification verification) {
        id = verification.getId();
        userId = verification.getUserId();
        articleId = verification.getArticleId();
        reason = verification.getReason();
        verificationDate = verification.getVerificationDate();
    }
}
