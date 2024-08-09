package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.SupportMapper;
import com.shihHsin.pojo.Support;
import com.shihHsin.service.ISupportService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SupportServiceImpl extends ServiceImpl<SupportMapper, Support> implements ISupportService {
    @Resource
    private SupportMapper supportMapper;

    @Override
    public Integer getSupportById(int id) {
        return null;
    }

    @Override
    public boolean supportEvidence(Integer evidenceId, Integer userId) {
        if(supportMapper.existByUserIdAndEvidenceId(userId, evidenceId)) {
            return false;
        }
        Support support = new Support();
        support.setUserId(userId);
        support.setEvidenceId(evidenceId);
        supportMapper.insert(support);
        return true;
    }

    @Override
    public boolean isSupportedByUserId(int id, Integer userId) {
//        LambdaQueryWrapper<Support> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Support::getEvidenceId, id).eq(Support::getUserId, userId);
//        Support support = supportMapper.selectOne(queryWrapper);
//        if(support!= null) {
//            return true;
//        }
        return supportMapper.existByUserIdAndEvidenceId(userId, id);
    }

    @Override
    public Integer countSupportByEvidenceId(int id) {
        LambdaQueryWrapper<Support> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Support::getEvidenceId, id);
        return  supportMapper.selectCount(queryWrapper).intValue();
    }

}
