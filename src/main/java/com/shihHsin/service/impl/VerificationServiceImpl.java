package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.VerificationMapper;
import com.shihHsin.pojo.Verification;
import com.shihHsin.service.IVerificationService;
import org.springframework.stereotype.Service;
@Service
public class VerificationServiceImpl extends ServiceImpl<VerificationMapper, Verification> implements IVerificationService {
}
