package com.shihHsin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.shihHsin.mapper")
@SpringBootApplication
public class SHApplication {
    public static void main(String[] args) {
        SpringApplication.run(SHApplication.class, args);
    }
}
