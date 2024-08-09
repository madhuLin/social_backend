package com.shihHsin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Support {
    @TableId(type = IdType.AUTO)
    private int id;
    private int userId;
    private int evidenceId;
}
