package com.shihHsin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shihHsin.pojo.Image;
import com.shihHsin.pojo.ImageEvidence;

import java.util.List;

public interface IImageEvidenceService extends IService<ImageEvidence> {
    List<Image>  findImagesByEvidenceId(int evidenceId);
}
