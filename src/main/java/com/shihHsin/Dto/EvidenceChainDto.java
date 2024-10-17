package com.shihHsin.Dto;

import lombok.Data;

@Data
public class EvidenceChainDto {
    private Integer evidenceId;
    private String transactionHash;
    private String authorAddress;
    private Long timestamp;
}
