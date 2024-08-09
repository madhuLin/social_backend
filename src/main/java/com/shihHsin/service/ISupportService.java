package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Support;

public interface ISupportService extends IService<Support> {
    Integer getSupportById(int id);

    boolean supportEvidence(Integer userId, Integer evidenceId);

    boolean isSupportedByUserId(int id, Integer userId);

    Integer countSupportByEvidenceId(int id);
}
