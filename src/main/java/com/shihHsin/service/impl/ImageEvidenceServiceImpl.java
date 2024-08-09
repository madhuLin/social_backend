package com.shihHsin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihHsin.mapper.ImageEvidenceMapper;
import com.shihHsin.pojo.Image;
import com.shihHsin.pojo.ImageEvidence;
import com.shihHsin.service.IImageEvidenceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageEvidenceServiceImpl extends ServiceImpl<ImageEvidenceMapper, ImageEvidence> implements IImageEvidenceService {

    @Resource
    private ImageEvidenceMapper imageEvidenceMapper;
    @Override
    public List<Image> findImagesByEvidenceId(int evidenceId) {

        return imageEvidenceMapper.findByEvidenceId(evidenceId);
    }
}
