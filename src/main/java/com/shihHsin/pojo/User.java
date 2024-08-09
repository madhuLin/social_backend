package com.shihHsin.pojo;


import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Data
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String email;
    private String sex;
    private Timestamp registrationDate;
    private String password;
    private String avatar;
    private String bio;
    private String intro;
    private boolean status;
}
