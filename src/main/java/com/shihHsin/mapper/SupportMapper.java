package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Support;
import org.apache.ibatis.annotations.Select;

public interface SupportMapper extends BaseMapper<Support> {
    @Select("SELECT COUNT(*) > 0 FROM support WHERE user_id = #{userId} AND evidence_id = #{evidenceId}")
    boolean existByUserIdAndEvidenceId(Integer userId, Integer evidenceId);
}
