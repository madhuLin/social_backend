package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Image;
import com.shihHsin.pojo.ImageEvidence;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ImageEvidenceMapper extends BaseMapper<ImageEvidence> {
    @Select("SELECT * FROM image_evidence WHERE evidence_id = #{evidenceId}")
    List<Image> findByEvidenceId(int evidenceId);
}
