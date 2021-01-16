package com.example.college;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.example.college.mapper")
public class CollegeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollegeApplication.class, args);
    }

}
