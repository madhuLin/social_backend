package com.shihHsin.Dto;

import lombok.Data;

@Data
public class VerificationChainDto {
    private Integer verificationId;
    private String transactionHash;
    private String authorAddress;
    private String title;
    private Long timestamp;
}
