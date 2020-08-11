package com.example.miaoshatest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.miaoshatest.dao")
public class MiaoshatestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshatestApplication.class, args);
    }

}
