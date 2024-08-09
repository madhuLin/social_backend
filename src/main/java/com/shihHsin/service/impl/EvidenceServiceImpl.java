package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.EvidenceMapper;
import com.shihHsin.pojo.Evidence;
import com.shihHsin.service.IEvidenceService;
import org.springframework.stereotype.Service;

@Service
public class EvidenceServiceImpl extends ServiceImpl<EvidenceMapper, Evidence> implements IEvidenceService {
}
