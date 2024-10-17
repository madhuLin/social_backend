package com.shihHsin.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserAvatarDto {
    private Integer id;
    private MultipartFile avatar;
}
