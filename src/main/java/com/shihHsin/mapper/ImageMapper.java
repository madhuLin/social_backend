package com.shihHsin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shihHsin.pojo.Image;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ImageMapper extends BaseMapper<Image> {
    @Select("SELECT * FROM image WHERE article_id = #{articleId}")
    List<Image> findByArticleId(int articleId);
}
